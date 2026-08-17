name: Deploy Documentation

on:
    workflow_dispatch:
    push:
        branches:
        - master
    paths:
    - 'documentation/**'
    - 'mkdocs.yml'
    - '.github/workflows/docs_deployment.yml'

permissions:
    contents: read
    pages: write
    id-token: write

jobs:
    deploy:
        runs-on: ubuntu-latest
        environment:
            name: github-pages
        url: ${{ steps.deployment.outputs.page_url }}
        steps:
        - uses: actions/checkout@v4

        - name: Build with Zensical
          uses: cssnr/zensical-action@v1
          with:
            upload: false
        
        - uses: actions/upload-pages-artifact@v3
          with:
            path: site/
        
        - id: deployment
          uses: actions/deploy-pages@v4