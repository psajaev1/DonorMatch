var express = require('express');
var app = express();
const uri = 'mongodb+srv://nicolas-isaza:Ni08291998@blood-xdwhh.mongodb.net/test?retryWrites=true&w=majority';
var mongoose = require('mongoose');

const DriveSchema = mongoose.Schema({
  name: String,
  capacity: Number,
  address: String,
  zip: Number,
  host: String
});

const UserSchema = mongoose.Schema({
  username: String,
  password: String,
  name: String
});

//CREATE DRIVE
app.use('/createDrive', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening createDrive');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
      console.log("Connection Successful");
      // compile schema to model
      var Drive = mongoose.model('drive', DriveSchema, 'Drives');
      // a document instance
      var drive1 = new Drive({ name: req.query.name, capacity: req.query.capacity, address: req.query.address, zip: req.query.zip, host: req.query.host});
      // save model to database
      drive1.save(function (err, drive) {
        if (err) return console.error(err);
        res.send(drive);
      });
   });
});

//CREATE ADMIN USER
app.use('/createAdmin', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening createAdmin');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
      console.log("Connection Successful");
      // compile schema to model
      var User = mongoose.model('user', UserSchema, 'AdminUsers');
      // a document instance
      var user1 = new User({ username: req.query.username, password: req.query.password, name: req.query.name });
      // save model to database
      user1.save(function (err, user) {
        if (err) return console.error(err);
        res.send(user);
      });
   });
});

//FIND USER
app.use('/findAdmin', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening findAdmin');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");
        // compile schema to model
        var User = mongoose.model('user', UserSchema, 'AdminUsers');
        // search for document instance
        User.find({ username: req.query.username }, function (err, data) {
          if (err) {
            console.log(err);
          }
          res.send(data);
          console.log(data);
        });
     });
  });

//EDIT PASSWORD
app.use('/editPassword', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening editPassword');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");

        db.collection("AdminUsers").updateOne({username: req.query.username}, {$set: {password: req.query.password}});

        // compile schema to model
        var User = mongoose.model('user', UserSchema, 'AdminUsers');
        // search for document instance
        User.find({ username: req.query.username }, function (err, data) {
          if (err) {
            console.log(err);
          }
          res.send(data);
          console.log(data);
        });
     });
  });

//EDIT NAME
app.use('/editName', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening editName');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");

        db.collection("AdminUsers").updateOne({username: req.query.username}, {$set: {name: req.query.name}});

        // compile schema to model
        var User = mongoose.model('user', UserSchema, 'AdminUsers');
        // search for document instance
        User.find({ username: req.query.username }, function (err, data) {
          if (err) {
            console.log(err);
          }
          res.send(data);
          console.log(data);
        });
     });
  });

//Start server
app.listen(3000, () => {
    console.log('Listening on port 3000');
});
