import React from "react";
import clsx from "clsx";
import { makeStyles } from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import Drawer from "@material-ui/core/Drawer";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import List from "@material-ui/core/List";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import IconButton from "@material-ui/core/IconButton";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import MenuIcon from "@material-ui/icons/Menu";
import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import ListItemText from "@material-ui/core/ListItemText";
import DashboardIcon from "@material-ui/icons/Dashboard";
import TimelineIcon from "@material-ui/icons/Timeline";
import LocalShippingIcon from "@material-ui/icons/LocalShipping";
import BorderOuterIcon from "@material-ui/icons/BorderOuter";
import GeofenceMap from "./GeofenceMap/GeofenceMap";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import VpnKeyIcon from "@material-ui/icons/VpnKey";
import Map from "./Map";
import AssetList from "./AssetList";
import TimelineMap from "./TimelineMap";
import { Link } from "react-router-dom";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
  },
  toolbar: {
    paddingRight: 24,
  },
  toolbarIcon: {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: "0 8px",
    ...theme.mixins.toolbar,
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  menuButtonHidden: {
    display: "none",
  },
  title: {
    flexGrow: 1,
  },
  drawerPaper: {
    position: "relative",
    whiteSpace: "nowrap",
    width: drawerWidth,
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerPaperClose: {
    overflowX: "hidden",
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    width: theme.spacing(7),
    [theme.breakpoints.up("sm")]: {
      width: theme.spacing(9),
    },
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    height: "100vh",
    overflow: "auto",
  },
  container: {
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4),
  },
  paper: {
    padding: theme.spacing(2),
    display: "flex",
    overflow: "auto",
    flexDirection: "column",
  },
  fixedHeight: {
    height: 240,
  },
}));

export default function Dashboard(props) {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);
  const [assetView, setAssetView] = React.useState(false);
  const [mapView, setMapView] = React.useState(true);
  const [timelineView, setTimelineView] = React.useState(false);
  const [geofencingView, setGeofencingView] = React.useState(false);
  const [assetId, setAssetId] = React.useState("");
  const [latitude, setLatitude] = React.useState(0);
  const [longitude, setLongitude] = React.useState(0);

  const handleTimelineView = () => {
    setAssetView(false);
    setMapView(false);
    setGeofencingView(false);
    setTimelineView(true);
  };

  const handleMapView = () => {
    setAssetView(false);
    setTimelineView(false);
    setGeofencingView(false);
    setMapView(true);
  };

  const handleAssetView = () => {
    setMapView(false);
    setTimelineView(false);
    setGeofencingView(false);
    setAssetView(true);
  };

  const handleGeofenceView = () => {
    setAssetView(false);
    setTimelineView(false);
    setMapView(false);
    setGeofencingView(true);
  };

  const sendDataToParent = (assetId, lat, lon) => {
    setAssetId(assetId);
    setLatitude(lat);
    setLongitude(lon);
    handleTimelineView();
  };

  const sendDataToParentforMap = (lat, lon) => {
    setLatitude(lat);
    setLongitude(lon);
    handleMapView();
  };

  const handleDrawerOpen = () => {
    setOpen(true);
  };
  const handleDrawerClose = () => {
    setOpen(false);
  };

  const logout = () => {
    localStorage.removeItem("userName");
    localStorage.removeItem("jsonToken");
    props.history.push("/");
  };

  const login = () => {
    props.history.push("/");
  };

  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar
        position="absolute"
        className={clsx(classes.appBar, open && classes.appBarShift)}
      >
        <Toolbar className={classes.toolbar}>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            className={clsx(
              classes.menuButton,
              open && classes.menuButtonHidden
            )}
          >
            <MenuIcon />
          </IconButton>
          <Typography
            component="h1"
            variant="h6"
            color="inherit"
            noWrap
            className={classes.title}
          >
            <Link to="/" style={{ textDecoration: "none", color: "white" }}>
              Asset Tracking Portal
            </Link>
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        classes={{
          paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
        }}
        open={open}
      >
        <div className={classes.toolbarIcon}>
          <IconButton onClick={handleDrawerClose}>
            <ChevronLeftIcon />
          </IconButton>
        </div>
        <Divider />
        <List>
          <div>
            <ListItem button onClick={handleMapView}>
              <ListItemIcon>
                <DashboardIcon />
              </ListItemIcon>
              <ListItemText primary="Dashboard" />
            </ListItem>
            <ListItem button onClick={handleAssetView}>
              <ListItemIcon>
                <LocalShippingIcon />
              </ListItemIcon>
              <ListItemText primary="Assets" />
            </ListItem>
            <ListItem button onClick={handleTimelineView}>
              <ListItemIcon>
                <TimelineIcon />
              </ListItemIcon>
              <ListItemText primary="Timeline" />
            </ListItem>
            <ListItem button onClick={handleGeofenceView}>
              <ListItemIcon>
                <BorderOuterIcon />
              </ListItemIcon>
              <ListItemText primary="Geofence" />
            </ListItem>
            {localStorage.getItem("userName") ? (
              <ListItem button onClick={logout}>
                <ListItemIcon>
                  <ExitToAppIcon />
                </ListItemIcon>
                <ListItemText primary="Logout" />
              </ListItem>
            ) : (
              <ListItem button onClick={login}>
                <ListItemIcon>
                  <VpnKeyIcon />
                </ListItemIcon>
                <ListItemText primary="Login" />
              </ListItem>
            )}
          </div>
        </List>
        <Divider />
      </Drawer>
      {localStorage.getItem("userName") ? (
        <main className={classes.content}>
          <div className={classes.appBarSpacer} />
          {assetView ? (
            <AssetList
              sendDataToParent={sendDataToParent}
              sendDataToParentforMap={sendDataToParentforMap}
            />
          ) : mapView ? (
            <Map
              sendDataToParent={sendDataToParent}
              lat={latitude}
              lon={longitude}
            />
          ) : timelineView ? (
            <TimelineMap assetId={assetId} lat={latitude} lon={longitude} />
          ) : geofencingView ? (
            <GeofenceMap
              assetId={"4e6d92ad-0b5e-4aaf-a322-3d614d966a9e"}
              lat={12.63426}
              lon={80.11253}
            />
          ) : null}
          {/* change the map above to timeline map */}
        </main>
      ) : (

          <div
            style={{
              display: "flex",
              overflow: "auto",
              flexDirection: "column",
            }}
          >
            <h2>Please Login</h2>
          </div>
      )}
    </div>
  );
}
