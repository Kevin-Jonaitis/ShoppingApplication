import React from 'react';
import { getFullImagePath } from './Api'

/**
 * A single item that we can purchuse. Holds all important display data. Makes callback to
 * app.js when we click on one to buy
 */
export default class Item extends React.Component {
  constructor(props) {
    super(props);
    this.state = { quantity: 1 }
  }
  handleInputChange = event =>
    this.setState({ [event.target.name]: event.target.value })

  render() {
    const { item } = this.props;
    const quantity = this.state.quantity;
    var addProductCountToCart = this.props.addProductCountToCart;
    return (
      <div className="card" style={{ marginBottom: "10px" }}>
        <div className="card-body">
          <div class="container">
            <div class="row">
              <div class="col-sm">
                <img width="200px" height="auto" src={getFullImagePath(item.filePath)} alt={item.name}></img>
              </div>
              <div class="col-8">
                <h4 className="card-title">{item.name}</h4>
                <p className="card-text">{item.description}</p>
              </div>
            </div>
            <div class="row float-right">
              <div class="col-sm">
                <h5 className="card-text"><small>price: </small>${item.price / 100} </h5>
              </div>
              <div class="col-8">

                <button className="btn btn-primary float-right"
                  onClick={() => addProductCountToCart(item.id, quantity)}>Add to cart
                    </button>
                <input type="number" value={this.state.quantity} name="quantity"
                  onChange={this.handleInputChange} className="float-right"
                  style={{ width: "60px", marginRight: "10px", borderRadius: "3px" }} />
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}
