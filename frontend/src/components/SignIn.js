import React from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import TextField from "@material-ui/core/TextField";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import Box from "@material-ui/core/Box";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import Copyright from "./Copyright";
import CustomAppBar from "./CustomAppBar";
import { withRouter } from "react-router-dom";
import { config } from "../App";

class SignIn extends React.Component {
  constructor() {
    super();
    this.state = {
      userName: "",
      password: "",
    };
  }

  validateInput = () => {
    let uname = this.state.userName;
    let pword = this.state.password;
    if (uname === "" || pword === "") return false;
    return true;
  };

  validateResponse = (errored, response) => {
    if (errored === true) {
      
      return false;
    }
    if (!response["jsonToken"]) {
      return false;
    }
    return true;
  };

  performAPICall = async () => {
    const settings = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        password: this.state.password,
        userName: this.state.userName,
      }),
    };
    let errored = false;
    let result = {};
    try {
      const fetchResponse = await fetch(
        config.endpoint + "/user/authenticate",
        settings
      );
      result = await fetchResponse.json();
    } catch (e) {
      errored = true;
    }
    if (this.validateResponse(errored, result)) {
      return result;
    }
  };

  persistLogin = async (jsonToken, userName) => {
    localStorage.setItem("userName", userName);
    localStorage.setItem("jsonToken", jsonToken);
  };

  login = async () => {
    if (this.validateInput()) {
      try {
        let result = await this.performAPICall();
        if (result) {
          let uname = this.state.userName;
          this.persistLogin(result.jsonToken, uname);
          this.setState({
            userName: "",
            password: "",
          });
          this.props.history.push("/dashboard");
        }
      } catch (e) {
        return e;
      }
    }
  };

  render() {
    return (
      <>
        return (
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <CustomAppBar history={this.props.history} />
          <Box mt={10} />

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Avatar>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <Box mt={2} />
            <form noValidate>
              <TextField
                autoComplete="userName"
                name="userName"
                variant="outlined"
                required
                fullWidth
                id="userName"
                label="User Name"
                autoFocus
                onChange={(e) => {
                  this.setState({
                    userName: e.target.value,
                  });
                }}
              />
              <Box mt={2} />
              <TextField
                variant="outlined"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="password"
                onChange={(e) => {
                  this.setState({
                    password: e.target.value,
                  });
                }}
              />
              <Box mt={2} />
              <Button
                fullWidth
                variant="contained"
                color="primary"
                onClick={this.login}
              >
                Sign In
              </Button>
              <Box mt={2} />
              <Grid container>
                <Grid item>
                  <Link href="register" variant="body2">
                    {"Don't have an account? Sign Up"}
                  </Link>
                </Grid>
              </Grid>
            </form>
          </div>
          <Box mt={8}>
            <Copyright />
          </Box>
        </Container>
        );
      </>
    );
  }
}

export default withRouter(SignIn);
