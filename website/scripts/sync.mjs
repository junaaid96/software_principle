// Generates website/content/ from the repo's Markdown (single source of truth).
// Never edit website/content by hand: it is wiped and rebuilt on every build.
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const ROOT = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../..');
const OUT = path.resolve(ROOT, 'website/content');
const GH = 'https://github.com/junaaid96/software_principle';
const KEEP = [];

// Wipe generated files but keep config/theme/public.
fs.mkdirSync(OUT, { recursive: true });
for (const e of fs.readdirSync(OUT)) if (!KEEP.includes(e)) fs.rmSync(path.join(OUT, e), { recursive: true });

const rel = (p) => path.relative(ROOT, p).split(path.sep).join('/');
const read = (p) => fs.readFileSync(p, 'utf8');

// repo-relative source path -> site route (without .md)
const pages = new Map();
const topics = fs.readdirSync(path.join(ROOT, 'topics')).filter((d) => /^\d\d-/.test(d)).sort();
for (const t of topics) pages.set(`topics/${t}/README.md`, `topics/${t}`);
for (const sub of ['adr', 'diagrams'])
  for (const f of fs.readdirSync(path.join(ROOT, 'docs', sub)).filter((f) => f.endsWith('.md')).sort())
    pages.set(`docs/${sub}/${f}`, `${sub}/${f.replace(/\.md$/, '')}`);
pages.set('docs/README.md', 'docs-overview');

function resolveLink(fromRel, href) {
  if (/^([a-z]+:|#|\/)/i.test(href)) return href;
  const [p, hash = ''] = href.split('#');
  const target = path.posix.normalize(path.posix.join(path.posix.dirname(fromRel), p)).replace(/\/$/, '');
  const h = hash ? `#${hash}` : '';
  for (const cand of [target, `${target}/README.md`]) if (pages.has(cand)) return `/${pages.get(cand)}${h}`;
  if (target === 'README.md' || target === '.') return `/${h}`;
  if (target === 'docs/adr') return `/adr/${h}`;
  if (target === 'docs/diagrams') return `/diagrams/${h}`;
  const abs = path.join(ROOT, target);
  const kind = fs.existsSync(abs) && fs.statSync(abs).isDirectory() ? 'tree' : 'blob';
  return `${GH}/${kind}/main/${target}${h}`;
}

// Rewrite links outside fenced code blocks.
function rewrite(md, fromRel) {
  let inFence = false;
  return md.split('\n').map((line) => {
    if (/^\s*(```|~~~)/.test(line)) { inFence = !inFence; return line; }
    if (inFence) return line;
    return line.replace(/\]\(([^)\s]+)\)/g, (_, h) => `](${resolveLink(fromRel, h)})`);
  }).join('\n');
}

const fm = (o) => `---\n${Object.entries(o).map(([k, v]) => `${k}: ${JSON.stringify(v)}`).join('\n')}\n---\n\n`;
const firstHeading = (md) => (md.match(/^#\s+(.+)$/m) || [, ''])[1].trim();
const plain = (s) => s.replace(/[*_`]/g, '').replace(/\[([^\]]*)\]\([^)]*\)/g, '$1');
const firstPara = (md) => {
  const para = md.split('\n\n').find((b) => b.trim() && !/^(#|\*\*Code|\||```|!\[|-)/.test(b.trim()));
  return plain((para || '').replace(/\s+/g, ' ').trim()).slice(0, 180);
};

function walk(dir) {
  if (!fs.existsSync(dir)) return [];
  return fs.readdirSync(dir, { withFileTypes: true }).sort((a, b) => a.name.localeCompare(b.name))
    .flatMap((e) => (e.isDirectory() ? walk(path.join(dir, e.name)) : e.name.endsWith('.java') ? [path.join(dir, e.name)] : []));
}

function sourceSection(md) {
  const m = md.match(/src\/main\/java\/dev\/junaidul\/principles\/[^)\s#]*/);
  if (!m) return '';
  let pkg = m[0].replace(/\/$/, '');
  if (pkg.endsWith('.java')) pkg = path.posix.dirname(pkg);
  const files = [...walk(path.join(ROOT, pkg)), ...walk(path.join(ROOT, pkg.replace('src/main/', 'src/test/')))];
  if (!files.length) return '';
  let s = `\n\n## Source code\n\nThe Java files for this topic, embedded straight from the repo. Browse the folder on [GitHub](${GH}/tree/main/${pkg}).\n`;
  for (const f of files) {
    const r = rel(f);
    const label = r.includes('/src/test/') || r.startsWith('src/test/') ? 'test' : 'source';
    s += `\n::: details ${path.basename(f)} <Badge type="${label === 'test' ? 'tip' : 'info'}" text="${label}" />\n[View on GitHub](${GH}/blob/main/${r})\n\n\`\`\`java\n${read(f).replace(/\n$/, '')}\n\`\`\`\n:::\n`;
  }
  return s;
}

function emit(route, srcRel, body, extra = {}) {
  const file = path.join(OUT, route === '' ? 'index.md' : route.endsWith('/') ? `${route}index.md` : `${route}.md`);
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, fm({ source: srcRel, ...extra }) + body + '\n');
}

const sidebarTopics = [];
for (const t of topics) {
  const srcRel = `topics/${t}/README.md`;
  const md = read(path.join(ROOT, srcRel));
  const title = firstHeading(md);
  emit(`topics/${t}`, srcRel, rewrite(md, srcRel) + sourceSection(md), { title: plain(title).replace(/^\d\d · /, ''), description: firstPara(md) });
  sidebarTopics.push({ num: t.slice(0, 2), text: plain(title).replace(' · ', '. '), link: `/topics/${t}` });
}
const side = { adr: [], diagrams: [] };
for (const [srcRel, route] of pages) {
  if (srcRel.startsWith('topics/')) continue;
  const md = read(path.join(ROOT, srcRel));
  emit(route, srcRel, rewrite(md, srcRel), { title: plain(firstHeading(md)), description: firstPara(md) });
  const sec = route.split('/')[0];
  if (side[sec]) side[sec].push({ text: plain(firstHeading(md)).replace(/^ADR (\d+):/, '$1.'), link: `/${route}` });
}
// Index pages for /adr/ and /diagrams/
for (const sec of ['adr', 'diagrams']) {
  const title = sec === 'adr' ? 'Architecture Decision Records' : 'Diagrams';
  emit(`${sec}/`, `docs/${sec}`, `# ${title}\n\n${side[sec].map((i) => `- [${i.text}](${i.link})`).join('\n')}`, { title });
}

// Home page: hero + the root README body (minus title and badges).
const readme = read(path.join(ROOT, 'README.md'));
const body = readme.split('\n').filter((l, i) => !(i === 0 && l.startsWith('# ')) && !/^\s*(\[!\[|!\[)/.test(l))
  .join('\n').replace(/^## Website[\s\S]*?(?=^## )/m, '');
const home = fm({ layout: 'home', source: 'README.md', title: 'Software engineering principles in Java 21', description: firstPara(body) })
  .replace(/---\n$/, `hero:
  name: "software_principle"
  text: "Software engineering principles, with runnable Java 21"
  tagline: "23 topic guides: SOLID, OOP, design patterns, architecture, DDD, testing, system design and more. Every code topic shows a BAD and a GOOD version, proven by JUnit 5 tests."
  actions:
    - theme: brand
      text: Start with SOLID
      link: /topics/01-solid
    - theme: alt
      text: Engineer checklist
      link: /topics/23-checklist
    - theme: alt
      text: View on GitHub
      link: ${GH}
features:
  - title: Fundamentals
    details: SOLID, OOP pillars, core principles (DRY, KISS, YAGNI, Law of Demeter) and GRASP.
    link: /topics/01-solid
  - title: Design
    details: OOAD with UML, Gang of Four patterns, architecture styles and Domain-Driven Design.
    link: /topics/05-ooad
  - title: Craft
    details: Clean code, refactoring, testing, API design, concurrency and security.
    link: /topics/09-clean-code
  - title: Systems and delivery
    details: Performance, observability, CI/CD, code review, Agile, system design, tech debt and ADRs.
    link: /topics/15-performance-scalability
---
`);
fs.writeFileSync(path.join(OUT, 'index.md'), home + '\n' + rewrite(body, 'README.md') + '\n');

fs.writeFileSync(path.resolve(ROOT, 'website/.vitepress/sidebar.generated.json'), JSON.stringify({ topics: sidebarTopics, ...side }, null, 2));
console.log(`synced ${topics.length} topics, ${side.adr.length} ADRs, ${side.diagrams.length} diagram pages`);
