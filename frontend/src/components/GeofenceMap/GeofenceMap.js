import React, { useState } from "react";
import MapGL, { Marker } from "react-map-gl";
import { Editor, DrawPolygonMode } from "react-map-gl-draw";
import inside from "point-in-polygon";
import CenterFocusStrongIcon from "@material-ui/icons/CenterFocusStrong";

import "./file.css";


const DEFAULT_VIEWPORT = {
  width: 800,
  height: 600,
  longitude: 23,
  latitude: 23,
  zoom: 3
};




const INIT_MARKERS = [
  { id: "f4fea437-232c-4be8-8d4c-b27534a0c43a", longitude: 79.98974, latitude: 12.54253, toggle: false },
  { id: "ee1fe05d-3c10-4b8f-888d-14683fded28d", longitude: 79.91267, latitude: 12.51165, toggle: false },
  { id: "eac76573-ae5c-4f87-8b5a-0a216d02ae8d", longitude: 79.83785, latitude: 12.4444, toggle: false }
];

export default function GeofenceMap() {
  const [viewport, setViewport] = useState(DEFAULT_VIEWPORT);
  const [markers, setMarkers] = useState(INIT_MARKERS);
  const [mode, setMode] = useState(null);
  const [features, setFeatures] = useState([]);

  const updateViewport = viewport => {
    setViewport(viewport);
  };

  const handleUpdate = val => {
    setFeatures(val.data);

    if (val.editType === "addFeature") {
      const polygon = val.data[0].geometry.coordinates[0];
      const newMarkers = markers.map((marker, i) => {
        const { longitude, latitude } = marker;
        const isInsidePolygon = inside([longitude, latitude], polygon);

        return { ...marker, toggle: isInsidePolygon };
      });

      setMarkers(newMarkers);
      setFeatures([]);
      setMode(null);
    }
  };

  const handleModeChange = () => {
    setMode(new DrawPolygonMode());
  };

  return (
    <MapGL
      {...viewport}
      width="100%"
      height="100%"
      mapboxApiAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
      mapStyle="mapbox://styles/code-monk08/ckmddvba4ami717lh2jfbwdm0"
      onViewportChange={updateViewport}
    >
      <Editor
        clickRadius={12}
        mode={mode}
        onUpdate={handleUpdate}
        features={features}
      />

      {markers.map(({ longitude, latitude, toggle, id }) => (
        <Marker key={id} latitude={latitude} longitude={longitude}>
          <span
            style={{
              backgroundColor: toggle ? "red" : "black",
              width: "10px",
              height: "10px",
              borderRadius: "50%",
              display: "block"
            }}
          />
        </Marker>
      ))}

      <div className="toolbar-wrapper">
        <div
          className={`tool-wrapper ${mode ? "active" : ""}`}
          onClick={handleModeChange}
        >
          <CenterFocusStrongIcon />
        </div>
      </div>
    </MapGL>
  );
}

