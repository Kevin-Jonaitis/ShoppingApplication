import React from 'react';
import Item from './Item';
import { getItems, updateCart } from './Api';
import { Link } from 'react-router-dom';

/**
 * A list of all the items. Pretty straightforward. Makes a call to our api to update the
 * list
 */
export default class ItemList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            items: []
        }
    }
    componentDidMount() {
        getItems().then((fetchedItems) => {
            this.setState({
                items: fetchedItems
            })
        })
    }

    render() {
        var addProductCount = this.props.addProductCountToCart;
        return (
            <div className="container">
                <h1>List of Available Items</h1><hr />
                {this.state.items.map((item, index) =>
                    <Item
                        item={item}
                        addProductCountToCart={addProductCount}
                        key={item.id} />)}
                <hr />
            </div>
        );
    }
}