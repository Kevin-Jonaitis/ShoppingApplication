import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react'
import Cart from '../Cart';
import { cart } from '../TestData'

/**
 * Some more sample tests
 */
test('when we have no items in cart, show cart with "no items" text', () => {
  const { cartApp } = render(<Cart cart={{}} />);
  const noItems = screen.getByText('No item', { exact: false });
  expect(noItems).toBeInTheDocument();
});

test('when we have no items in cart, the total value is calculated correctly', () => {
  const { cartApp, debug } = render(<Cart cart={cart} />);
  const totalAmount = screen.getByTestId("totalCost")

  expect(totalAmount).toHaveTextContent("$14")
  console.log(totalAmount)
});