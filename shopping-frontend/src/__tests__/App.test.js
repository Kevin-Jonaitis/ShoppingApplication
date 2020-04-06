import React from 'react';
import { render, screen, fireEvent, getAllByText } from '@testing-library/react';
import App from '../App';
import axios from 'axios'

import { items, cart } from '../TestData'

import { getItems, getCart, updateCart, logout } from '../Api'


jest.mock('../Api')

/**
 * A sample of some tests I would write for the application.
 */
test("when changing the value of items and clicking on modify count, we should make a call to downstream to update the cart with the new value", async () => {

  getItems.mockResolvedValueOnce(items)
  getCart.mockResolvedValueOnce(cart);
  const { getByText, debug } = render(<App />);
  const modifyCount = await screen.findAllByTestId('numberOfItem')
    .then((results) => {

      const valueToUpdate = "10";
      const firstItemInputBox = results[0]
      fireEvent.input(results[0]);

      //Change the count of items we have
      fireEvent.change(firstItemInputBox, { target: { value: valueToUpdate } });

      //Click on the modify button
      fireEvent.click(screen.getAllByText("Modify count")[0]);

      const updatedCart = [];
      Object.assign(updatedCart, cart);

      // Verify we call update cart, and verify we update it with the new values we st
      expect(updateCart).toHaveBeenCalledWith(cart);
      expect(cart["123"][1]).toEqual(valueToUpdate);
    }
    )
});
