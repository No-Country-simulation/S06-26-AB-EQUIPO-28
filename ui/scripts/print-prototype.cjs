const fs = require('fs');
const data = JSON.parse(fs.readFileSync('C:\\Users\\LENOVO\\Documents\\7.CodeMyProjects\\HackathonNoCountry\\appbit\\ui\\design\\openpencil\\appbit-prototype.op', 'utf8'));

function printTree(node, indent = 0) {
  const prefix = '  '.repeat(indent);
  const name = node.name || node.type || 'unnamed';
  let role = '';
  if (node.role) { role = ' [role:' + node.role + ']'; }
  let content = '';
  if (node.content) { content = ' "' + node.content.substring(0,50) + '"'; }
  console.log(prefix + '-' + name + role + content);
  if (node.children) {
    node.children.forEach(c => printTree(c, indent + 1));
  }
}

data.pages.forEach(page => {
  console.log('========== PAGE: ' + page.name + ' ==========');
  page.children.forEach(child => printTree(child));
});