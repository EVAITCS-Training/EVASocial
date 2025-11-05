//var
//global scoped variable
//let
//block scoped variable, that can be changed
//const
//block scoped variable, immutable
const posts = [];

function postStatus(e) {
  e.preventDefault()
  const status = document.getElementById("status-post").value;
  const username = document.getElementById("username").value;
  const post = {
    "status": status,
    "username": username,
    "like": false,
    "dislike": false
  };
  posts.push(post);
  console.table(post)
  console.table(posts)
}