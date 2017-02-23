'use strict';

var axios = require('axios');
var imagemagick = require('imagemagick-native');
var fs = require('fs');
var request = require('request');
var FormData = require('form-data');

var fileName = 'temp.png';
var download = function download(uri, filename, callback) {
    request.head(uri, function (err, res, body) {
        console.log('content-type:', res.headers['content-type']);
        console.log('content-length:', res.headers['content-length']);
        request(uri).pipe(fs.createWriteStream(filename)).on('close', callback);
    });
};

download('http://images.feirasenegocios.com.br/kWKVGiXVvCPA5VC6UH6rQvoNGZM=/351x0/top/media.feirasenegocios.com.br/media/archives/2017/02/14/2784652102-expo-revestir-2017.jpg', fileName, function () {
    console.log('done');
    setTimeout(cropImage, 1000);
});

function uploadBanner() {
    console.log('Upload Banner');
    var fd = new FormData();

    setTimeout(function () {
        fd.append('upload_4x3', fs.readFileSync('blob_4x3.jpg'), 'blob_4x3.jpg');
        fd.append('upload_16x9', fs.readFileSync('blob_16x9.jpg'), 'blob_16x9.jpg');
    }, 3000);

    var config = { headers: { 'Content-Type': 'multipart/form-data' } };

    var URL_UPLOAD_BANNER = '/event/upload';

    axios.post(URL_UPLOAD_BANNER, fd, config).then(function (res) {
        return console.log('Banner Uploaded', res.data);
    }).catch(function (err) {
        return console.log('Error upload banner', err);
    });
}

function cropImage() {
    console.log('Crop Image');
    //getImageDimensions('temp.png');

    imagemagick.identify({
        srcData: fs.readFileSync(fileName)
    }, function (err, result) {
        var width = result.width;
        var height = result.height;
        var ratio16x9 = 16 / 9;
        var ratio4x3 = 4 / 3;

        var min = Math.max(width, height);
        var newWidth16x9 = min === width ? width : min / ratio16x9;
        var newHeight16x9 = min === height ? height : min / ratio16x9;

        var newWidth4x3 = min === width ? width : min / ratio4x3;
        var newHeight4x3 = min === height ? height : min / ratio4x3;

        /*console.log('newWidth', newWidth16x9);
        console.log('newHeight', newHeight16x9);
        console.log('ratio:', newWidth16x9/newHeight16x9);*/

        fs.writeFileSync('blob_16x9.jpg', imagemagick.convert({
            srcData: fs.readFileSync(fileName),
            width: newWidth16x9,
            height: newHeight16x9,
            resizeStyle: 'aspectfill',
            gravity: 'Center'
        }));

        fs.writeFileSync('blob_4x3.jpg', imagemagick.convert({
            srcData: fs.readFileSync(fileName),
            width: newWidth4x3,
            height: newHeight4x3,
            resizeStyle: 'aspectfill',
            gravity: 'Center'
        }));

        uploadBanner();
    });
};

/*var file = fs.createWriteStream("temp.jpg");
getFile.get('http://images.feirasenegocios.com.br/kWKVGiXVvCPA5VC6UH6rQvoNGZM=/351x0/top/media.feirasenegocios.com.br/media/archives/2017/02/14/2784652102-expo-revestir-2017.jpg')
.then(function(response) {
    fs.writeFile('logo.jpg', response.data, function(err){
            if (err) throw err
            console.log('File saved.')
    });
  console.log('opa');
  console.log(response);
})
.catch(error => {
    console.log(error);
});*/

/*
headers: {
    'Content-Type': imageFile.type
}
headers: { 'content-type': 'multipart/form-data' }
*/

axios.defaults.baseURL = 'http://localhost:9000';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.common['Accept'] = 'application/json';

var URL_INSERT_LOCAL = '/locals/create';
var URL_CITY = '/locals/city';
var URL_LOGIN = '/login';
var URL_LOCAL = '/locals/search';
var URL_CREATE_SECTORS = '/blueprint/sectors';
var URL_CREATE_EVENT = '/event/create';

var credentials = { key: 'inafalcao@gmail.com', password: '83a4d8be816c962698fe7bf46139f1aa' };
axios.post(URL_LOGIN, credentials).then(function (response) {
    axios.defaults.headers.common['X-AUTH-TOKEN'] = response.data.result.token;
}).catch(function (error) {
    console.log('Erro ao logar');
    console.log(error.data);
});

/*setTimeout(() => { insertEvents(); }, 3000)

function insertEvents() {
    var events = require('./events.json');
    events.forEach( e => insertLocal(e) );
}

function insertLocal(eventData) {
    // [1] Inserir o local e recuperar seu ID
    axios.post(URL_INSERT_LOCAL, eventData.event.local)
    .then(response => {
       console.log('Iseriu local ' + eventData.event.local.name);
       return findCityAndLocal(eventData);
     })
     .catch(function (error) {
       console.log('deu erro em tudo');
       console.log(error.response.status)
       return findCityAndLocal(eventData);
     });

}

function findCityAndLocal(eventData) {
    console.log('vendo se entrou');
    axios.all([
        axios.get(encodeURI(URL_CITY + '/' + eventData.event.cityName + '/' + eventData.event.cityUF)),
        axios.get(encodeURI(URL_LOCAL + '?q=' + eventData.event.local.name))
      ])
      .then(axios.spread( (cityRes,  localRes) => {
        console.log('City Res: ', cityRes.data.result.id);
        console.log('Local Res: ', localRes.data.result.id);
        eventData.event.localId = localRes.data.result.id;
        eventData.event.local.id = localRes.data.result.id;
        eventData.event.cityId = localRes.data.result.id;
        return insertSector(eventData);
      }))
      .catch(error => {
        cosole.log('Erro findCityAndLocal', error);
      });
}

function insertSector(eventData) {
    var sector = { name: eventData.sessions[0].sectors[0].name, localId: eventData.event.localId } ;
    var sectors = {sectors: [sector]} ;
    console.log('insertSector');
    axios.post(URL_CREATE_SECTORS, sectors)
    .then(res => {
        console.log('Sector inserted', res.data.result[0].id);
        eventData.sessions[0].sectors[0].id = res.data.result[0].id;
        insertEventAndSessions(eventData);
    })
    .catch(error => {
        console.log('Error insert sector', error.response.status);
    });
}

function insertEventAndSessions(eventData) {
    console.log('Inserting Event');
    axios.post(URL_CREATE_EVENT, eventData)
    .then(res => console.log(res.data) )
    .catch(error => console.log('Error insert Event', error.response) );
}
*/
