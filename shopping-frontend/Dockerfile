# base image
FROM node:12.2.0-alpine

# set working directory
WORKDIR /app


COPY package.json package-lock.json ./
RUN npm install

# Copy the main application
COPY src src

#copy the public folder b/c we need that too
COPY public public

EXPOSE 3000

# # add `/app/node_modules/.bin` to $PATH
# ENV PATH /app/node_modules/.bin:$PATH

# # install and cache app dependencies
# COPY package.json /app/package.json
# RUN npm install
# RUN npm install react-scripts@3.0.1 -g

# start app
CMD ["npm", "start"]