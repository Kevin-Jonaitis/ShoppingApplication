/**
 * Simple data to be used in unit tests
 */
const itemOne = {
    "id": "123",
    "name": "Eggs",
    "description": "A dozen eggs. Plain and simple. A versatile weapon in the kitchen.",
    "price": 500,
    "filePath": "/images/eggs.jpg"
}
const itemTwo =
{
    "id": "456",
    "name": "Hand Sanitizer",
    "description": "A squirt a day keeps the COVID-19 away. Use sparingly, it's in short supply",
    "price": 100,
    "filePath": "/images/handSanitizer.jpg"
};
export const cart = {
    123: [itemOne, 2],
    456: [itemTwo, 4]
};


export const cartGetApiResponse = {
    "itemList": [itemOne, itemTwo],
    "itemCount": [2, 4]
};

export const items = [itemOne, itemTwo];