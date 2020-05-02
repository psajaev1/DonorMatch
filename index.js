var express = require('express');
var app = express();
const uri = 'mongodb+srv://nicolas-isaza:Ni08291998@blood-xdwhh.mongodb.net/test?retryWrites=true&w=majority';
var mongoose = require('mongoose');

const DriveSchema = mongoose.Schema({
  name: String,
  capacity: Number,
  address: String,
  zip: Number,
  host: String,
  start: Date,
  end: Date
});


const UserSchema = mongoose.Schema({
  username: String,
  password: String,
  name: String
});

const PeopleSchema = mongoose.Schema({
  name: String,
  age: Number,
  password: String,
  start_dates: [Date],
  end_dates: [Date]
})


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
      var drive1 = new Drive({ name: req.query.name, capacity: req.query.capacity, address: req.query.address, 
        zip: req.query.zip, host: req.query.host, start: req.query.start, end:req.query.end});
      // save model to database
      drive1.save(function (err, drive) {
        if (err) return console.error(err);
        res.send(drive);
      });
   });
});

//FIND DRIVE
app.use('/findDrive', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening findAdmin');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");
        // compile schema to model
        var Drive = mongoose.model('drive', DriveSchema, 'Drives');
        // search for document instance
        Drive.find({ name: req.query.name }, function (err, data) {
          if (err) {
            console.log(err);
          }
          res.send(data);
          console.log(data);
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

//EDIT ADMIN PASSWORD
app.use('/editAdminPassword', (req, res) => {
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

//EDIT ADMIN NAME
app.use('/editAdminName', (req, res) => {
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

//GET DRIVE HISTORY
app.use('/getDriveHistory', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening findAdmin');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");
        // compile schema to model
        var Drive = mongoose.model('drive', DriveSchema, 'Drives');
        // search for document instance
        Drive.find({ host: req.query.username }, function (err, data) {
          if (err) {
            console.log(err);
          }
          res.send(data);
          console.log(data);
        });
     });
  });

  //DELETE DRIVE
  app.use('/deleteDrive', (req, res) => {
      res.header("Access-Control-Allow-Origin", "*");
      mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening delete drive');});
      var db = mongoose.connection;
      db.on('error', console.error.bind(console, 'connection error:'));
      db.once('open', function() {
          console.log("Connection Successful");
          // compile schema to model
          var Drive = mongoose.model('drive', DriveSchema, 'Drives');
          // search for document instance
          Drive.deleteOne({ "name" : req.query.name }, function (err, data) {
            if (err) {
              console.log("DELETEONE error:" + err);
            }
            res.send(data);
            console.log(data);
          });
       });
    });

    //EDIT DRIVE
    app.use('/editDrive', (req, res) => {
        res.header("Access-Control-Allow-Origin", "*");
        mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening createDrive');});
        var db = mongoose.connection;
        db.on('error', console.error.bind(console, 'connection error:'));
        db.once('open', function() {
          console.log("Connection Successful");
          // compile schema to model
          var Drive = mongoose.model('drive', DriveSchema, 'Drives');
          // save model to database
          Drive.replaceOne({ "name" : req.query.name },
          { name: req.query.newname, capacity: req.query.capacity, address: req.query.address, zip: req.query.zip, host: req.query.host },
          function (err, drive) {
            if (err) return console.error(err);
            res.send(drive);
          });
       });
    });

    //GET ALL USERS
    app.use('/getAllPeople', (req, res) => {
            res.header("Access-Control-Allow-Origin", "*");
            mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening getPerson');});
            var db = mongoose.connection;
            db.on('error', console.error.bind(console, 'connection error:'));
            db.once('open', function() {
                console.log("Connection Successful");
                // compile schema to model
                var Person = mongoose.model('People', PeopleSchema, 'people');
                // search for document instance
                Person.find({}, function (err, data) {
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