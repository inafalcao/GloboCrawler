var axios = require('axios')

/*var axios = axios.create({
  baseURL: 'http://localhost:9000',
  timeout: 30000,
  headers: {'Content-Type': 'application/json'}
});*/

axios.defaults.baseURL = 'http://localhost:9000';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.common['Accept'] = 'application/json';

const URL_INSERT_LOCAL = '/locals/create';
const URL_CITY = '/locals/city';
const URL_LOGIN = '/login';

const credentials = {key: 'inafalcao@gmail.com', password: '83a4d8be816c962698fe7bf46139f1aa'};
axios.post(URL_LOGIN, credentials)
    .then(response => {
        axios.defaults.headers.common['X-AUTH-TOKEN'] = response.data.result.token;
    })
    .catch(error => {
        console.log('Erro ao logar');
        console.log(error)
    });

setTimeout(() => { insertEvents(); }, 3000)

function insertEvents() {
    var events = require('./events.json');
    events.forEach( e => insertLocal(e) );
}

function insertLocal(event) {
    // [1] Inserir o local e recuperar seu ID
    axios.post(URL_INSERT_LOCAL, event.local)
    .then(response => {
       console.log('Iseriu local');
       var localId = response.data.result;
       delete event.local;
       event.localId = localId;
       return findCityId(event);
     })

     .then(response => { // success get city
        event.cityId = response.data.result.id
        console.log('city id: ' + event.cityId);
        console.log('timezoneId: ' + response.data.result.timeZoneId);
        // cadastrar setor único do inferno

     })

     .catch(function (error) {
       console.log('deu erro em tudo');
       console.log(error)
     });

}

function findCity(event) {
    axios.get(URL_CITY + '/' + event.cityName + '/' + event.cityUF)
}

function insertSector() {

}

