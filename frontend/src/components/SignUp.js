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


class SignUp extends React.Component {
  constructor() {
    super();
    this.state = {
      emailId: "",
      mobileNo: "",
      password: "",
      username: "",
    };
  }

  validateInput = () => {
    let uname = this.state.username;
    let email = this.state.emailId;
    let pword = this.state.password;
    let mobno = this.state.mobileNo;
    let reg = new RegExp(
      /^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i
    );
    if (!reg.test(email)) return false;
    if (uname === "" || pword === "") return false;
    if (uname.length < 6 || uname.length > 32) return false;
    if (pword.length < 6 || pword.length > 32) return false;
    if (mobno.length !== 10) return false;

    return true;
  };

  validateResponse = (errored, response) => {
    if (errored === true) {
      return false;
    }
    if (!response["userId"]) {
      return false;
    }
    return true;
  };

  performAPICall = async () => {
    let myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    let raw = JSON.stringify({
      emailId: this.state.emailId,
      mobileNo: this.state.mobileNo,
      password: this.state.password,
      username: this.state.username,
    });
    var requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    let errored = false;
    let result = {};
    try {
      const fetchResponse = await fetch(
        config.endpoint + "/user/register",
        requestOptions
      );
      result = await fetchResponse.json();
    } catch (e) {
      console.log(e);
      errored = true;
    }
    if (this.validateResponse(errored, result)) {
      return result;
    }
  };

  register = async () => {
    if (this.validateInput()) {
      try {
        let result = await this.performAPICall();
        if (result) {
          this.setState({
            emailId: "",
            mobileNo: "",
            password: "",
            username: "",
          });
          this.props.history.push("/login");
        }
      } catch (e) {
        console.log(e);
        return e;
      }
    }
  };

  render() {
    return (
      <>
        {/* TODO: include custom header */}
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
            {/* upper div className={classes.paper} */}
            <Avatar>
              {/* upper avatar  className={classes.avatar} */}
              <LockOutlinedIcon />
            </Avatar>

            <Typography component="h1" variant="h5">
              Register
            </Typography>
            <Box mt={2} />
            <form noValidate>
              {/* upper form className={classes.form} */}
              <Grid container spacing={2}>
                {/* email */}
                <Grid item xs={12}>
                  <TextField
                    variant="outlined"
                    required
                    fullWidth
                    id="emailId"
                    label="Email Address"
                    name="emailId"
                    autoComplete="emailId"
                    onChange={(e) => {
                      this.setState({
                        emailId: e.target.value,
                      });
                    }}
                  />
                </Grid>
                {/* mobile */}
                <Grid item xs={12} sm={6}>
                  <TextField
                    autoComplete="mobileNo"
                    name="mobileNo"
                    variant="outlined"
                    required
                    fullWidth
                    id="mobileNo"
                    label="Mobile Number"
                    autoFocus
                    onChange={(e) => {
                      this.setState({
                        mobileNo: e.target.value,
                      });
                    }}
                  />
                </Grid>
                {/* username */}
                <Grid item xs={12} sm={6}>
                  <TextField
                    autoComplete="username"
                    name="username"
                    variant="outlined"
                    required
                    fullWidth
                    id="username"
                    label="User Name"
                    autoFocus
                    onChange={(e) => {
                      this.setState({
                        username: e.target.value,
                      });
                    }}
                  />
                </Grid>
                {/* password */}
                <Grid item xs={12}>
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
                </Grid>
              </Grid>
              <Box mt={2} />
              <Button
                fullWidth
                variant="contained"
                color="primary"
                onClick={this.register}
              >
                {/* upper button className={classes.submit} */}
                Sign Up
              </Button>
              <Box mt={2} />
              <Grid container justify="flex-end">
                <Grid item>
                  <Link href="login" variant="body2">
                    Already have an account? Sign in
                  </Link>
                </Grid>
              </Grid>
            </form>
          </div>
          <Box mt={5}>
            <Copyright />
          </Box>
        </Container>
      </>
    );
  }
}

export default withRouter(SignUp);
