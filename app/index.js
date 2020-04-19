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
app.use('/findDrive', (req, res) => {
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

//CREATE USER
app.use('/createUser', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening createUser');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
      console.log("Connection Successful");
      // compile schema to model
      var User = mongoose.model('user', UserSchema, 'people');
      // a document instance
      var user1 = new User({ name: req.query.name, password: req.query.password, username: req.query.username});
      // save model to database
      user1.save(function (err, user) {
        if (err) return console.error(err);
        res.send(user);
      });
   });
});

//FIND USER
app.use('/findUser', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening findUser');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");
        // compile schema to model
        var User = mongoose.model('user', UserSchema, 'people');
        // search for document instance
        User.findOne({ username: req.query.name }, function (err, data) {
          if (err) {
            console.log(err);
          }
          res.send(data);
          console.log(data);
        });
     });
  });


//EDIT USER
app.use('/editUser', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening editName2');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");

        db.collection("people").updateOne({username: req.query.username}, {$set: {name: req.query.name}});

        // compile schema to model
        var User = mongoose.model('user', UserSchema, 'people');
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


      //add dates to person
      app.use('/addDate', (req, res) => {
          res.header("Access-Control-Allow-Origin", "*");
          mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening addDate');});
          var db = mongoose.connection;

          var start_date_string = req.query.start_dates;
          var end_date_string = req.query.end_dates;
          var start_date = new Date(start_date_string);
          var end_date = new Date(end_date_string);
          console.log(start_date);
          console.log(end_date);

          db.on('error', console.error.bind(console, 'connection error:'));
          db.once('open', function() {
              console.log("Connection Successful");

              db.collection("people").updateOne({name: req.query.name}, {$addToSet: {start_dates: start_date}});
              db.collection("people").updateOne({name: req.query.name}, {$addToSet: {end_dates: end_date}});


              // compile schema to model
              var Person = mongoose.model('people', UserSchema, 'people');
              // search for document instance
              Person.find({ name: req.query.name }, function (err, data) {
                if (err) {
                  console.log(err);
                }
                res.send(data);
                console.log(data);
              });
           });
        });


//FIND PERSON
app.use('/getPerson', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening getPerson');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");
        // compile schema to model
        var Person = mongoose.model('People', PeopleSchema, 'people');
        // search for document instance
        Person.findOne({ name: req.query.name }, function (err, data) {
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