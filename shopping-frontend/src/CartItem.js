import React from 'react';
import { getFullImagePath } from './Api'

/**
 * Holds individual items that were added to the cart. Invokes callbacks to app.js when
 * modifying the cart count
 */
export default class CartItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      count: 1,
      initialCount: props.count
    }
  }

  handleInputChange = event => this.setState({ [event.target.name]: event.target.value })

  render() {
    const item = this.props.item;
    const initialCount = this.props.count;
    const count = this.state.count;
    return (
      <div className="card" style={{ marginBottom: "10px" }}>
        <div className="card-body">
          <h4 className="card-title">{item.name}</h4>
          <div class="row">
            <div class="col-sm">
              <img width="200px" height="auto" src={getFullImagePath(item.filePath)} alt={item.name}></img>
            </div>
            <div class="col-8">
              {/* <h4 className="card-title">{item.name}</h4> */}
            </div>
          </div>

          <h5 className="card-text"><small>price: </small>${item.price / 100}</h5>
          <span className="card-text text-success">
            <small>Quantity: </small>{initialCount}</span>
          <input data-testid="numberOfItem" type="number" value={count} name="count"
            onChange={this.handleInputChange} className="float-right"
            style={{ width: "60px", marginRight: "10px", borderRadius: "3px" }} />
          <button className="btn btn-primary float-right"
            onClick={() => this.props.modifyProductCount(item.id, count)}>Modify count</button>
        </div>
      </div>
    )
  }
}