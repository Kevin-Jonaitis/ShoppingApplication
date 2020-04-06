import React from 'react';
import { login, register } from './Api'

/**
 * Handles login/lougout functionality. The login data is stored in an "authToken" field 
 * in local storage, and is passed as a header to the authenticated endpoints.
 * When we logout, we delete that data. 
 * 
 * We can both register and login on this page; when either are done, we store that new token.
 */
export default class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = { name: '', password: '', button: '' };
  }
  handleInputChange = (event) =>
    this.setState({ [event.target.name]: event.target.value })
  submitLogin = (event) => {
    event.preventDefault();

    if (this.state.button == "login") {
      login(this.state, this.props.cart)
        .then(token => window.location = '/').catch(err => console.log(err));
    } else if (this.state.button == "signup") {
      register(this.state, this.props.cart)
        .then(token => window.location = '/').catch(err => console.log(err));
    }
  }
  render() {
    const cart = this.props.cart;
    return (
      <div className="container">
        <hr />
        <div className="col-sm-8 col-sm-offset-2">
          <div className="panel panel-primary">
            <div className="panel-heading"><h3>Log in </h3></div>
            <div className="panel-body">
              <form onSubmit={(event) => this.submitLogin(event)}>
                <div className="form-group">
                  <label>Name:</label>
                  <input type="text" className="form-control"
                    name="userName" onChange={this.handleInputChange} />
                </div>
                <div className="form-group">
                  <label>Password:</label>
                  <input type="password" className="form-control"
                    name="password" onChange={this.handleInputChange} />
                </div>
                <div class="row">
                  <div class="col-sm">
                    <button 
                    type="submit" 
                    className="btn btn-primary" 
                    name="abc" 
                    onClick={() => this.setState({ "button": "login" })}>Login</button>
                  </div>
                  <div class="col-16">
                    <button 
                    type="submit" 
                    className="btn btn-primary" 
                    name="cde" 
                    onClick={() => this.setState({ "button": "signup" })}>Sign up</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    );
  }
}