var axios = require('axios')
var imagemagick = require('imagemagick-native');
var fs = require('fs');
var request = require('request');
var FormData = require('form-data');
//require('babel-polyfill');

axios.defaults.baseURL = 'http://localhost:9000';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.common['Accept'] = 'application/json';

const URL_INSERT_LOCAL = '/locals/create';
const URL_CITY = '/locals/city';
const URL_LOGIN = '/login';
const URL_LOCAL = '/locals/search'
const URL_CREATE_SECTORS = '/blueprint/sectors';
const URL_CREATE_EVENT = '/event/create';

var token = '';
const fileName = 'temp.jpg';

const credentials = {key: 'inafalcao@gmail.com', password: '83a4d8be816c962698fe7bf46139f1aa'};
axios.post(URL_LOGIN, credentials)
     .then(response => {
         console.log('success login');
         token = response.data.result.token;
         axios.defaults.headers.common['X-AUTH-TOKEN'] = response.data.result.token;
     })
     .catch(error => {
         console.log('Erro ao logar');
         console.log(error.data)
     });


setTimeout(() => insertEvents(), 3000);

function insertEvents() {
    console.log('aqui.');
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
        console.log(eventData);
        download(eventData.event.banner, fileName, () => {
          console.log('done');
          setTimeout( () => {
            cropImage.then(response => {
              delete eventData.event.banner;
              eventData.event.photoBanner.prefix = response.prefix;
              eventData.event.photoBanner.suffix = response.suffix;
              console.log('deu tudo certo');
            })
            .catch(error => {
              console.log('veish');
            });
          }
          ,1000);
        });
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


/* ============== DOWNLOAD/CROP/UPLOAD ==================== */

var download = function(uri, filename, callback){
  request.head(uri, function(err, res, body){
    //console.log('content-type:', res.headers['content-type']);
    //console.log('content-length:', res.headers['content-length']);
    request(uri).pipe(fs.createWriteStream(filename)).on('close', callback);
  });
};

/*download('testeminin', fileName, function(){
  console.log('done');
  setTimeout(cropImage, 1000);
});*/

var uploadBanner = new Promise( (resolve, reject) => {
    console.log('Upload Banner')
    var fd = new FormData();
    //var buffer = new Buffer(file.data, 'utf-8');
    var file1 = fs.createReadStream('blob_4x3.jpg');
    var file2 = fs.createReadStream('blob_16x9.jpg');

    fd.append('upload_4x3', file1, 'blob_4x3.jpg');
    fd.append('upload_16x9', file2, 'blob_16x9.jpg');

    var r = request.post('http://localhost:9000/event/upload', {headers: {'X-AUTH-TOKEN': token} }, function optionalCallback (err, httpResponse, body) {
      if (err) {
        reject('Error uploading banner');
        //return console.error('upload failed:', err);
      }
      resolve(body);
      //console.log('Upload successful!  Server responded with:', body);
    })
    var form = r.form()
    form.append('upload_4x3', file1, 'blob_4x3.jpg');
    form.append('upload_16x9', file2, 'blob_16x9.jpg');

});


var cropImage = new Promise( (resolve, reject) => {
    console.log('Crop Image');

    imagemagick.identify({
        srcData: fs.readFileSync(fileName)
    }, function (err, result) {
        const width = result.width;
        const height = result.height;
        const ratio16x9 = 16/9;
        const ratio4x3 = 4/3;

        const min = Math.max(width, height);
        const newWidth16x9 = (min === width) ? width : (min / ratio16x9) ;
        const newHeight16x9 = (min === height) ? height : (min / ratio16x9);

        const newWidth4x3 = (min === width) ? width : (min / ratio4x3) ;
        const newHeight4x3 = (min === height) ? height : (min / ratio4x3);

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

        uploadBanner.then(res => resolve({ prefix: res.result.prefix, suffix: res.result.suffix }), err => reject(err));

    });

});
