var express = require('express');
var app = express();
const uri = 'mongodb+srv://nicolas-isaza:Ni08291998@blood-xdwhh.mongodb.net/test?retryWrites=true&w=majority';
var mongoose = require('mongoose');

const DriveSchema = mongoose.Schema({
  name: String,
  capacity: Number,
  address: String,
  zip: Number
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
      var drive1 = new Drive({ name: req.query.name, capacity: req.query.capacity, address: req.query.address, zip: req.query.zip });
      // save model to database
      drive1.save(function (err, drive) {
        if (err) return console.error(err);
        res.send(drive.name + " saved to Drives collection.");
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
        res.send(user.name + " saved to a collection.");
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


//get Blood drives
app.use('/get', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening getDrive');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open',function() {
        console.log("Connection Successful");
        var Drive = mongoose.model('drive', DriveSchema, 'Drives');

        Drive.findOne({ name: req.query.name}, function (err, data) {
            if (err) {
                console.log(err);
            }
            console.log(data);
            res.send(data);
            });
        });
    });


//Start server
app.listen(3000, () => {
  console.log('Listening on port 3000');
});