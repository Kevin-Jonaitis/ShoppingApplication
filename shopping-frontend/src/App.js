import React from 'react';
import logo from './logo.svg';
import Item from './Item';
import ItemList from './ItemList';
import Cart from './Cart';
import Login from './Login';
import './App.css';
import { BrowserRouter as Router, Link, Route, Redirect } from 'react-router-dom';
import { getItems, isAuthenticated, getCart, updateCart } from './Api.js';
import { Form, FormControl, Button, Navbar, Nav, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';


class App extends React.Component {
  constructor(props) {
    super(props);
    /**
     * We store cart and item state here as they'll be used by both the product and
     * cart components
     */
    this.state = {
      cart: [],
      items: []
    };
  }

  logOut() {
    localStorage.removeItem('authtoken');
    this.setState({
      cart: [],
    });
  }


  componentDidMount() {
    getItems().then((items) => this.setState({ items }));
    getCart().then((cart) => {
      return this.setState({ cart })
    });
  }

  /**
   * Modifies the count of that product we have in our cart
   */
  modifyProductCount(productId, count) {
    const modifiedCart = this.state.cart;
    modifiedCart[productId][1] = count;

    //Update our backend/DB
    updateCart(modifiedCart);

    // Delete if we clear the values
    if (modifiedCart[productId][1] == 0) {
      delete modifiedCart[productId];
    }
    this.setState({
      cart: modifiedCart,
    });

  }

  /**
   * Adds the count to the number of that specific product we have in our cart
   */
  addProductCountToCart(productId, count) {
    const modifiedCart = [];
    Object.assign(modifiedCart, this.state.cart);
    var items = this.state.items;

    if (modifiedCart[productId] == undefined) {
      //Not the best data modelling; 
      // ideally we'd have the items be a map as returned from backend
      for (var i = 0; i < items.length; i++) {
        if (items[i].id == productId) {
          modifiedCart[productId] = [items[i], count];
        }
      }
    } else {
      modifiedCart[productId][1] = modifiedCart[productId][1] + count;
    }

    this.setState({
      cart: modifiedCart,
    });

    // Update our backend/DB
    updateCart(modifiedCart);
  }

  updateLocalCartState(cart) {
    this.setState({
      cart: cart,
    });
  }

  render() {
    const auth = isAuthenticated();
    const cart = this.state.cart;
    return (
      <Router>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <div className="container">
            <Link className="navbar-brand" to="/">ShoppingCart</Link>
            <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
              <div className="navbar-nav">
                <Link className="nav-item nav-link" to="/">Cart</Link>
                <Link className="nav-item nav-link" to="/items">Items</Link>
                {(auth) ?
                  (<a className="nav-item nav-link" href="/"
                    onClick={this.logOut}>Log out</a>) :
                  (<Link className="nav-item nav-link float-right"
                    to="/login">Log in</Link>)
                }
              </div>
            </div>
          </div>
        </nav>
        <div className="container">
          <br />
          <Route exact path="/">
            <Cart
              cart={cart}
              modifyProductCount={(productId, count) => this.modifyProductCount(productId, count)} />
          </Route>
          <Route exact path="/items">
            <ItemList
              cart={cart}
              addProductCountToCart={(productId, count) => this.addProductCountToCart(productId, count)} />
          </Route>
          {(!auth) ? (
            <Route exact path="/login">
              <Login cart={cart}></Login>
            </Route>) : ''}
        </div>
      </Router>
    );
  }
}

export default App;
