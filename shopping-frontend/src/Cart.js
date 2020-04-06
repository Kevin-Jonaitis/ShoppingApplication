import React from 'react';
import { Redirect, Link } from 'react-router-dom';
import { getCart } from './Api';
import { isAuthenticated } from './Api';
import centsToDollars from './HelperFunctions'
import CartItem from './CartItem';
import Item from './Item';

/**
 * Contains all the cartItems, as well as has some rendered data related to totals
 */
export default class Cart extends React.Component {
  constructor(props) {
    super(props);
  }

  calculateTotalAndUpdateState() {
    var total = 0;
    Object.values(this.props.cart)
      .map((itemTuple, index) => {
        total = total + (centsToDollars(itemTuple[0].price * itemTuple[1]));
      })

    return total;
  }

  render() {
    var totalCost = this.calculateTotalAndUpdateState();
    const cart = Object.values(this.props.cart); // just an array of the values
    return (
      <div className=" container">
        <h3 className="card-title">Cart</h3><hr />
        {
          cart.map((itemTuple, index) =>
            <CartItem
              item={itemTuple[0]}
              count={itemTuple[1]}
              modifyProductCount={this.props.modifyProductCount}
              key={index} />)
        } <hr />
        {cart.map((itemTuple, index) =>
          <div name="cartItem" key={index}>
            <p>{itemTuple[0].name} <small> (quantity: {itemTuple[1]})</small>
              <span className="float-right text-primary">${centsToDollars(itemTuple[1] * itemTuple[0].price)}
              </span></p><hr />
          </div>
        )} <hr />
        {cart.length ?
          <div><h4><small>Total Amount:</small><span data-testid="totalCost" className="float-right text-primary">
            ${totalCost}</span></h4><hr /></div> : ''}
        {!cart.length ? <h3 className="text-warning">No item on the cart</h3> : ''}
      </div>
    );
  }
}