import { defineConfig } from 'vitepress';
import { withMermaid } from 'vitepress-plugin-mermaid';
import sidebarData from './sidebar.generated.json' with { type: 'json' };

const GH = 'https://github.com/junaaid96/software_principle';
const SITE = 'https://junaaid96.github.io/software_principle/';
const group = (text: string, from: number, to: number) => ({
  text,
  collapsed: false,
  items: sidebarData.topics.filter((t) => +t.num >= from && +t.num <= to).map(({ text, link }) => ({ text, link })),
});

export default withMermaid(defineConfig({
  srcDir: 'content',
  base: '/software_principle/',
  lang: 'en-US',
  title: 'software_principle',
  titleTemplate: ':title · Software Principles',
  description: 'A hands-on curriculum for software engineering principles, object-oriented design and best practices, with runnable Java 21 examples and JUnit 5 tests.',
  cleanUrls: true,
  lastUpdated: true,
  sitemap: { hostname: SITE },
  head: [
    ['meta', { name: 'theme-color', content: '#3c8772' }],
    ['meta', { property: 'og:type', content: 'website' }],
    ['meta', { property: 'og:site_name', content: 'software_principle' }],
    ['meta', { name: 'twitter:card', content: 'summary' }],
  ],
  transformPageData(page) {
    const route = page.relativePath.replace(/(^|\/)index\.md$/, '$1').replace(/\.md$/, '');
    const title = page.frontmatter.title || page.title || 'software_principle';
    const desc = page.frontmatter.description || page.description || '';
    page.frontmatter.head ??= [];
    page.frontmatter.head.push(
      ['link', { rel: 'canonical', href: SITE + route }],
      ['meta', { property: 'og:title', content: title }],
      ['meta', { property: 'og:url', content: SITE + route }],
      ['meta', { property: 'og:description', content: desc }],
    );
  },
  markdown: { lineNumbers: true },
  themeConfig: {
    nav: [
      { text: 'Learning path', link: '/' },
      { text: 'Topics', link: '/topics/01-solid' },
      { text: 'Checklist', link: '/topics/23-checklist' },
      { text: 'Diagrams', link: '/diagrams/' },
      { text: 'ADRs', link: '/adr/' },
    ],
    sidebar: [
      group('Fundamentals', 1, 4),
      group('Design', 5, 8),
      group('Craft', 9, 14),
      group('Systems and delivery', 15, 23),
      { text: 'Diagrams', collapsed: true, items: sidebarData.diagrams },
      { text: 'Decision records', collapsed: true, items: sidebarData.adr },
    ],
    socialLinks: [{ icon: 'github', link: GH }],
    search: { provider: 'local' },
    outline: { level: [2, 3] },
    editLink: {
      pattern: ({ frontmatter }) => `https://github.com/junaaid96/software_principle/blob/main/${frontmatter.source ?? ''}`,
      text: 'View source on GitHub',
    },
    footer: { message: 'Released under the MIT License.', copyright: '© 2026 Md. Junaidul Islam' },
  },
  mermaid: { theme: 'default' },
}));
