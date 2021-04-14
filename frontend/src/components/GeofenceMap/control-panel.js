import React from "react";
import area from '@turf/area'

function ControlPanel(props) {
    const polygon = props.polygon;
    const polygonArea = polygon && area(polygon);
    return (
        <div className="control-panel">
            <h3>Draw Region</h3>
            {polygon && (
                <p>
                    {polygonArea} <br />
                    square meters
                </p>
            )}
        </div>
    );
}

export default React.memo(ControlPanel);