import React, { useState, useEffect } from "react";
import ReactMapGL, { Marker, Popup } from "react-map-gl";
import * as assetLocation from "../data/timeline.json";
import DirectionsWalkIcon from "@material-ui/icons/DirectionsWalk";
import LocalShippingIcon from "@material-ui/icons/LocalShipping";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import { createMuiTheme } from "@material-ui/core/styles";

const theme = createMuiTheme();

export default function TimelineMap(props) {
  const [viewport, setViewport] = useState({
    latitude: props.lat, //get latitude info from places which clicked on view timeline button
    longitude: props.lon, //get longitude info from places which clicked on view timeline button
    width: "100vw", //this is fine
    height: "100vh", //this is fine
    zoom: 17, //this is fine
  });

  let customid = props.assetId;
  console.log(customid);

  const [selectedAsset, setSelectedAsset] = useState(null);
  const [timeOne, setTimeOne] = useState("");
  const [timeTwo, setTimeTwo] = useState("");

  const handleChangeTimeOne = (event) => {
    setTimeOne(event.target.value);
  };

  const handleChangeTimeTwo = (event) => {
    setTimeTwo(event.target.value);
  };

  function getAssetObject() {
    return assetLocation.features.filter((data) => data.id === customid)[0];
  }

  const handleFiltering = () => {
    if (timeOne !== "" && timeTwo !== "") {
      let startDate = new Date(timeOne).getTime();
      let endDate = new Date(timeTwo).getTime();
      let result = getAssetObject().locations.filter((d) => {
        let time = new Date(d.time).getTime();
        if (startDate < endDate) {
          return (startDate < time && time < endDate);
        }
        else{
          return (endDate < time && time < startDate);
        }
      });
      return result;
    }
    return getAssetObject().locations;
  };

  useEffect(() => {
    const listener = (e) => {
      if (e.key === "Escape") {
        setSelectedAsset(null);
      }
    };
    window.addEventListener("keydown", listener);

    return () => {
      window.removeEventListener("keydown", listener);
    };
  }, []);

  if (!customid) {
    return (
      <div
        style={{
          padding: theme.spacing(2),
          display: "flex",
          overflow: "auto",
          flexDirection: "column",
        }}
      >
        <h2>Please View this Page using View Timeline Buttons</h2>
      </div>
    );
  } else {
    return (
      <>
        <div>
          <FormControl
            variant="outlined"
            style={{
              margin: theme.spacing(1),
              minWidth: 120,
            }}
          >
            <InputLabel id="time-one">Time One</InputLabel>
            <Select
              labelId="time-one"
              id="time-one"
              value={timeOne}
              onClick={handleChangeTimeOne}
              label="Time One"
            >
              {getAssetObject().locations.map((assetTimeline) => (
                <MenuItem value={assetTimeline.time}>
                  {assetTimeline.time}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl
            variant="outlined"
            style={{
              margin: theme.spacing(1),
              minWidth: 120,
            }}
          >
            <InputLabel id="time-two">Time Two</InputLabel>
            <Select
              labelId="time-two"
              id="dtime-two"
              value={timeTwo}
              onClick={handleChangeTimeTwo}
              label="Time Two"
            >
              {getAssetObject().locations.map((assetTimeline) => (
                <MenuItem value={assetTimeline.time}>
                  {assetTimeline.time}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </div>
        <div>
          <ReactMapGL
            {...viewport}
            mapboxApiAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
            mapStyle="mapbox://styles/code-monk08/ckmddvba4ami717lh2jfbwdm0"
            onViewportChange={(viewport) => {
              setViewport(viewport);
            }}
          >
            {handleFiltering().map((assetTimeline) => (
              <Marker
                key={customid}
                latitude={assetTimeline.lat}
                longitude={assetTimeline.lon}
              >
                <button
                  className="marker-btn"
                  onClick={(e) => {
                    e.preventDefault();
                    setSelectedAsset(assetTimeline);
                  }}
                >
                  {getAssetObject().assetType === "truck" ? (
                    <LocalShippingIcon />
                  ) : (
                    <DirectionsWalkIcon />
                  )}
                </button>
              </Marker>
            ))}

            {selectedAsset ? (
              <Popup
                latitude={selectedAsset.lat}
                longitude={selectedAsset.lon}
                onClose={() => {
                  setSelectedAsset(null);
                }}
              >
                <div>
                  <h2>{getAssetObject().assetName}</h2>
                  <p>
                    <strong>Asset Type</strong>: {getAssetObject().assetType}
                  </p>
                  <p>
                    <strong>Latitude</strong>: {selectedAsset.lat}
                  </p>
                  <p>
                    <strong>Longitude</strong>: {selectedAsset.lon}
                  </p>
                  <p>
                    <strong>Time</strong>: {selectedAsset.time}
                  </p>
                </div>
              </Popup>
            ) : null}
          </ReactMapGL>
        </div>
      </>
    );
  }
}
