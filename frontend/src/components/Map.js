import React, { useState, useEffect } from "react";
import ReactMapGL, { Marker, Popup } from "react-map-gl";
import DirectionsWalkIcon from "@material-ui/icons/DirectionsWalk";
import LocalShippingIcon from "@material-ui/icons/LocalShipping";
import BlurOnSharpIcon from "@material-ui/icons/BlurOnSharp";
import axios from "axios";
import { config } from "../App";

export default function Map({ sendDataToParent }) {
  const [viewport, setViewport] = useState({
    latitude: 12.51811,
    longitude: 79.94316,
    width: "100vw",
    height: "100vh",
    zoom: 8,
  });
  const [selectedAsset, setSelectedAsset] = useState(null);
  const [assetLocation, setAssetLocation] = useState([]);
  useEffect(() => {
    let configuration = {
      method: "GET",
      url: `${config.endpoint}/gps/assets`,
      headers: {
        "Access-Control-Allow-Origin": "*",
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    };

    axios(configuration)
      .then(function (response) {
        setAssetLocation(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });

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

  return (
    <div>
      <ReactMapGL
        {...viewport}
        mapboxApiAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
        mapStyle="mapbox://styles/code-monk08/ckmddvba4ami717lh2jfbwdm0"
        onViewportChange={(viewport) => {
          setViewport(viewport);
        }}
      >
        {assetLocation.map((asset) => (
          <Marker
            key={asset.id}
            latitude={asset.locations[0].lat}
            longitude={asset.locations[0].lon}
          >
            <button
              className="marker-btn"
              onClick={(e) => {
                e.preventDefault();
                setSelectedAsset(asset);
              }}
            >
              {asset.assetType === "truck" ? (
                <LocalShippingIcon />
              ) : (
                <DirectionsWalkIcon />
              )}
            </button>
          </Marker>
        ))}

        {selectedAsset ? (
          <Popup
            latitude={selectedAsset.locations[0].lat}
            longitude={selectedAsset.locations[0].lon}
            onClose={() => {
              setSelectedAsset(null);
            }}
          >
            <div>
              <BlurOnSharpIcon />
              <h2>{selectedAsset.assetName}</h2>

              <p>
                <strong>Asset Type</strong>: {selectedAsset.assetType}
              </p>
              <p>
                <strong>Latitude</strong>: {selectedAsset.locations[0].lat}
              </p>
              <p>
                <strong>Longitude</strong>: {selectedAsset.locations[0].lon}
              </p>
              <button
                className="marker-btn"
                onClick={() => {
                  sendDataToParent(
                    selectedAsset.id,
                    selectedAsset.locations[0].lat,
                    selectedAsset.locations[0].lon
                  );
                }}
              >
                View Timeline
              </button>
            </div>
          </Popup>
        ) : null}
      </ReactMapGL>
    </div>
  );
}
