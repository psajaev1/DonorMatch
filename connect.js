const url = "http://localhost:3000/";

function getAsync(endpoint, callback) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
     return callback(this.response);
    }
  };
  xhttp.open("GET", url + endpoint, true);
  xhttp.send();
}

function postAsync(endpoint, callback) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
     return callback(this.response);
    }
  };
  xhttp.open("POST", url + endpoint, true);
  xhttp.send();
}

function isValid() {
  console.log("goood");

}
