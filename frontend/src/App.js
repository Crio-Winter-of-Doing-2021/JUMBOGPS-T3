import { Route, Switch } from "react-router-dom";
import React from "react";
import "./App.css";

import Landing from "./components/Landing.js";
import SignIn from "./components/SignIn";
import SignUp from "./components/SignUp";
import Dashboard from "./components/Dashboard";
import AssetList from "./components/AssetList";

export const config = {
  endpoint: "https://asset-tracker-backend.herokuapp.com/api/v1",
};

function App() {
  return (
    <div style={{ overflowX: "hidden", overflowY: "hidden" }}>
      <Switch>
        <Route path="/register" component={SignUp} />
        <Route path="/login" component={SignIn} />
        <Route path="/dashboard" component={Dashboard} />
        <Route path="/assetList" component={AssetList} />
        <Route component={Landing} />
        <Route exact path="/" component={Landing} />
      </Switch>
    </div>
  );
}

export default App;
