# Docs website

The site at https://junaaid96.github.io/software_principle/ is built with [VitePress](https://vitepress.dev) from the repo's own Markdown, so `topics/`, `docs/` and the root `README.md` stay the single source of truth.

- `scripts/sync.mjs` copies those files into `content/` (git-ignored), rewrites links, and embeds each topic's Java sources.
- `.vitepress/config.mts` holds the sidebar, search, Mermaid and SEO settings.
- `.github/workflows/pages.yml` builds and deploys on every push to `main` that touches the docs or sources.

```bash
cd website
npm ci
npm run dev      # http://localhost:5173/software_principle/
npm run build    # output in .vitepress/dist
```
