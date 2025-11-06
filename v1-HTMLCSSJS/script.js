//var
//global scoped variable
//let
//block scoped variable, that can be changed
//const
//block scoped variable, immutable
const posts = [
  {
    "status": "Just finished my morning coffee! Ready to tackle the day ☕",
    "username": "coffeelover42",
    "like": false,
    "dislike": false
  },
  {
    "status": "Working on a new JavaScript project. Loving the challenge!",
    "username": "coderninja",
    "like": true,
    "dislike": false
  },
  {
    "status": "Beautiful sunset today. Nature never fails to amaze me 🌅",
    "username": "naturephoto",
    "like": true,
    "dislike": false
  },
  {
    "status": "Anyone else struggling with CSS grid? Need some help here!",
    "username": "webdev_newbie",
    "like": false,
    "dislike": false
  },
  {
    "status": "Finally deployed my first full-stack app! Feels amazing 🚀",
    "username": "fullstack_dev",
    "like": true,
    "dislike": false
  },
  {
    "status": "Monday blues hitting hard today... Need more motivation",
    "username": "workinghard123",
    "like": false,
    "dislike": true
  },
  {
    "status": "Learning React hooks and they're actually pretty cool!",
    "username": "react_learner",
    "like": true,
    "dislike": false
  },
  {
    "status": "Pizza for lunch again. I really need to start cooking 🍕",
    "username": "foodie_life",
    "like": false,
    "dislike": false
  }
];

function postStatus(e) {
  e.preventDefault();
  const status = document.getElementById("status-post").value;
  const username = document.getElementById("username").value;
  const post = {
    "status": status,
    "username": username,
    "like": false,
    "dislike": false
  };
  posts.push(post);
  console.table(post);
  console.table(posts);
  viewPosts();
  saveToLocalStorage();
}

function viewPosts() {
  const statusData = document.getElementById("status-data");
  statusData.innerHTML = "";
  posts.slice().reverse().forEach((post, idx) => {
    const statusRow = document.createElement("div");
    const id = posts.length - 1 - idx;
    statusRow.innerHTML = `<p>${post.username}</p><p>${post.status}</p><button onclick="deletePost(${id})">Delete</button>`;
    statusData.appendChild(statusRow);
  });
}

const deletePost = function(id) {
  posts.splice(id, 1);
  saveToLocalStorage();
  viewPosts();
}

/* const viewPosts = () => {
 const statusData = document,getElementById("status-data");
 statusData.
}*/

const saveToLocalStorage = () => {
  localStorage.setItem("statusPosts", JSON.stringify(posts));
}

const getFromLocalStorage = () => {
  if (localStorage.getItem("statusPosts") === null) return;
  posts.length = 0;
  JSON.parse(localStorage.getItem("statusPosts")).forEach(post => {
    posts.push(post);
  });
}

document.addEventListener("DOMContentLoaded", getFromLocalStorage);
document.addEventListener("DOMContentLoaded", viewPosts);