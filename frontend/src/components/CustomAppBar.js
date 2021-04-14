import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  title: {
    flexGrow: 1,
  },
}));

export default function CustomAppBar() {

  
  const classes = useStyles();
  return (
    <>
      <AppBar position="absolute">
        <Toolbar>
          <Typography
            component="h1"
            variant="h6"
            color="secondary"
            noWrap
            className={classes.title}
          >
            <Link to="/" style={{ textDecoration: "none", color: "white" }}>
              Asset Tracking Portal
            </Link>
          </Typography>
        </Toolbar>
      </AppBar>
    </>
  );
}
