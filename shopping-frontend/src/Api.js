import axios from 'axios';
import Cookies from 'js-cookie';
import ItemList from './ItemList';
const BASE_URL = 'http://localhost:8080';
const AUTH_TOKEN = 'authtoken';

let config = { headers: { authtoken : localStorage.getItem(AUTH_TOKEN)  }}

export function getItems() {
        return axios.get(`${BASE_URL}/items`)
                .then(response => response.data)
                .catch(function (error) {
                    // handle error
                    console.log(error);
                  });
}

/**
 * Grabs the cart, and returns it as a Map<productID, Tuple<Item, Count>>
 */
export function getCart() {

    //If user isn't authenticated, simply return an empty array
    if (!isAuthenticated()) {

        return new Promise(() => []) 
    }
    return axios.get(`${BASE_URL}/cart`, config)
            .then((response) => {
                // let cart = item[item]
                let itemCount = response.data["itemCount"];
                let mapOfProductToItemCountTuple = {};
                response.data["itemList"].map((item, index) => {
                    mapOfProductToItemCountTuple[item.id] = [item, itemCount[index]];
                })
                return mapOfProductToItemCountTuple;
            })
            .catch(function (error) {
                console.log(error);
              });
}

/* Append the proper base url to the image path so we can fetch it */
export function getFullImagePath(filePath) {
    return BASE_URL + "/" + filePath;
}

export function updateCart(cart) {

    // If the user isn't authenticated, simply return early;
    if (!isAuthenticated()) {
        return;
    }
    var productIdToCount = {}
    Object.values(cart).map((item) => {
        productIdToCount[item[0].id] = parseInt(item[1]); 
    });

    return axios.put(`${BASE_URL}/cart`, productIdToCount, config)
    .then(response => { return response.data});
}

export function login(data, cart) {
    return axios.post(`${BASE_URL}/users/login`, 
                      { userName: data.userName, password: data.password })
      .then(response => {
          localStorage.setItem(AUTH_TOKEN, response.headers[AUTH_TOKEN]);
          config.headers[AUTH_TOKEN] = response.headers[AUTH_TOKEN]

          //We also update the cart for the now logged-in user
          updateCart(cart); 
      })
      .catch(err => Promise.reject(err.data));
}

/**
 * We simply delete the auth cookie
 */
export function logout() {
    localStorage.removeItem(AUTH_TOKEN);
}

export function register(data, cart) {
    return axios.post(`${BASE_URL}/users/register`, 
                      { userName: data.userName, password: data.password })
      .then(response =>  {
            localStorage.setItem(AUTH_TOKEN, response.headers[AUTH_TOKEN])
            config.headers[AUTH_TOKEN] = response.headers[AUTH_TOKEN]
            //We also update the cart for the now logged-in user
            updateCart(cart); 
      })
      .catch(err => Promise.reject(err.data));
  }

export function isAuthenticated(){
    var authenticated = localStorage.getItem(AUTH_TOKEN) != undefined;
    return authenticated;
}