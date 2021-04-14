import React from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import { Link } from "react-router-dom";
import Box from "@material-ui/core/Box";
import MapIcon from "@material-ui/icons/Map";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";
import Copyright from "./Copyright";
import CustomAppBar from "./CustomAppBar";

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: "100%",
    marginTop: theme.spacing(1),
  },
  title: {
    flexGrow: 1,
  },
  submit: {
    margin: theme.spacing(1, 0, 2),
  },
}));

export default function Landing(props) {
  const classes = useStyles();

  const logout = () => {
    localStorage.removeItem("userName");
    localStorage.removeItem("jsonToken");
    props.history.push("/");
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <CustomAppBar />
      <Box mt={10} />

      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <MapIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Welcome to Jumbotail Asset Tracker
        </Typography>

        {localStorage.getItem("userName") ? (
          <>
            <Link to="/dashboard">
              <Button
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
              >
                Dashboard
              </Button>
            </Link>

            <Link to="/">
              <Button
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
                onClick={logout}
              >
                Logout
              </Button>
            </Link>
          </>
        ) : (
          <>
            <Link to="/login">
              <Button
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
              >
                Login
              </Button>
            </Link>

            <Link to="/register">
              <Button
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
              >
                Register
              </Button>
            </Link>
          </>
        )}
      </div>
      <Box mt={8}>
        <Copyright />
      </Box>
    </Container>
  );
}
