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

//CREATE USER
app.use('/createPerson', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening createPerson');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
      console.log("Connection Successful");
      // compile schema to model
      var Person = mongoose.model('newPerson', PeopleSchema, 'people');
      // a document instance
      var person1 = new Person({ name: req.query.name, age: req.query.age, password: req.query.password});
      // save model to database
      person1.save(function (err, user) {
        if (err) return console.error(err);

        res.send(person1);
      });
   });
});

//FIND USER
app.use('/findPerson', (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    mongoose.connect(uri, {useNewUrlParser: true, useUnifiedTopology: true}, () => {console.log('listening findPerson');});
    var db = mongoose.connection;
    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {
        console.log("Connection Successful");
        // compile schema to model
        var People = mongoose.model('newperson', PeopleSchema, 'people');
        // search for document instance
        People.findOne({ name: req.query.name }, function (err, data) {
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
