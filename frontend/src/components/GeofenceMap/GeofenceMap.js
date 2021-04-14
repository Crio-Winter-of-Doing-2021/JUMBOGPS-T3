import MapGL from "react-map-gl";
import ControlPanel from "./control-panel";
import React, { useState, useRef, useCallback } from "react";
import { getFeatureStyle, getEditHandleStyle } from "./style";
import { Editor, DrawPolygonMode, EditingMode } from "react-map-gl-draw";

export default function GeofenceMap(props) {
  const [viewport, setViewport] = useState({
    latitude: props.lat,
    longitude: props.lon,
    width: "100vw",
    height: "100vh",
    zoom: 10,
  });

  const [mode, setMode] = useState(null);
  const [selectedFeatureIndex, setSelectedFeatureIndex] = useState(null);
  const editorRef = useRef(null);

  const onSelect = useCallback((options) => {
    setSelectedFeatureIndex(options && options.selectedFeatureIndex);
  }, []);

  const onDelete = useCallback(() => {
    if (selectedFeatureIndex !== null && selectedFeatureIndex >= 0) {
      editorRef.current.deleteFeatures(selectedFeatureIndex);
    }
  }, [selectedFeatureIndex]);

  const onUpdate = useCallback(({ editType }) => {
    if (editType === "addFeature") {
      setMode(new EditingMode());
    }
  }, []);

  const drawTools = (
    <div className="mapboxgl-ctrl-top-left">
      <div className="mapboxgl-ctrl-group mapboxgl-ctrl">
        <button
          className="mapbox-gl-draw_ctrl-draw-btn mapbox-gl-draw_polygon"
          title="Polygon tool (p)"
          onClick={() => setMode(new DrawPolygonMode())}
        />
        <button
          className="mapbox-gl-draw_ctrl-draw-btn mapbox-gl-draw_trash"
          title="Delete"
          onClick={onDelete}
        />
      </div>
    </div>
  );

  const features = editorRef.current && editorRef.current.getFeatures();
  const selectedFeature =
    features &&
    (features[selectedFeatureIndex] || features[features.length - 1]);

  return (
    <>
      <div>
        <MapGL
          {...viewport}
          mapboxApiAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
          mapStyle="mapbox://styles/code-monk08/ckmddvba4ami717lh2jfbwdm0"
          onViewportChange={(viewport) => {
            setViewport(viewport);
          }}
        >
          <Editor
            ref={editorRef}
            style={{ width: "100%", height: "100%" }}
            clickRadius={12}
            mode={mode}
            onSelect={onSelect}
            onUpdate={onUpdate}
            editHandleShape={"circle"}
            featureStyle={getFeatureStyle}
            editHandleStyle={getEditHandleStyle}
          />
          {drawTools}
        </MapGL>
        <ControlPanel polygon={selectedFeature} />
      </div>
    </>
  );
}
