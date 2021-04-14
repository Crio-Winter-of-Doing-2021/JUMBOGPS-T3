import React, { useEffect, useState } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import { createMuiTheme } from "@material-ui/core/styles";
import { config } from "../App";
import axios from "axios";

const theme = createMuiTheme();

export default function AssetList({
  sendDataToParent,
  sendDataToParentforMap,
}) {
  const [assetLocation, setAssetLocation] = useState([]);
  const [filteredData, setFilteredData] = React.useState(assetLocation);
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
        setFilteredData(response.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  }, []);

  const [assetType, setAssetType] = React.useState("");

  const handleChange = (event) => {
    setAssetType(event.target.value);
    if (assetType === "salesperson") {
      let salespersonData = assetLocation.filter(
        (data) => data.assetType === "salesperson"
      );
      setFilteredData(salespersonData);
    } else if (assetType === "truck") {
      let truckData = assetLocation.filter(
        (data) => data.assetType === "truck"
      );
      setFilteredData(truckData);
    } else {
      setFilteredData(assetLocation);
    }
  };

  return (
    <>
      <Grid item xs={12}>
        <Paper
          style={{
            padding: theme.spacing(2),
            display: "flex",
            overflow: "auto",
            flexDirection: "column",
          }}
        >
          <Typography component="h2" variant="h6" color="primary" gutterBottom>
            All Assets
          </Typography>

          <div>
            <FormControl
              variant="outlined"
              style={{
                margin: theme.spacing(1),
                minWidth: 120,
              }}
            >
              <InputLabel id="demo-simple-select-outlined-label">
                Asset Type
              </InputLabel>
              <Select
                labelId="demo-simple-select-outlined-label"
                id="demo-simple-select-outlined"
                value={assetType}
                onClick={handleChange}
                label="AssetType"
              >
                <MenuItem value={"truck"}>Truck</MenuItem>
                <MenuItem value={"salesperson"}>Salesperson</MenuItem>
              </Select>
            </FormControl>
          </div>

          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Asset Name</TableCell>
                <TableCell>Asset Type</TableCell>
                <TableCell>Last Recorded Latitude</TableCell>
                <TableCell>Last Recorded Longitude</TableCell>
                <TableCell>Last Recorded Time</TableCell>
                <TableCell>View on Dashboard</TableCell>
                <TableCell>View Timeline</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredData.map((asset) => {
                return (
                  <>
                    <TableRow key={asset.id}>
                      <TableCell>{asset.assetName}</TableCell>
                      <TableCell>{asset.assetType}</TableCell>
                      <TableCell>{asset.locations[0].lat}</TableCell>
                      <TableCell>{asset.locations[0].lon}</TableCell>
                      <TableCell>{asset.locations[0].time}</TableCell>
                      <TableCell>
                        <button
                          onClick={() => {
                            sendDataToParentforMap(
                              asset.locations[0].lat,
                              asset.locations[0].lon
                            );
                          }}
                          className="marker-btn"
                        >
                          View on Dashboard
                        </button>
                      </TableCell>
                      <TableCell>
                        <button
                          onClick={() => {
                            sendDataToParent(
                              asset.id,
                              asset.locations[0].lat,
                              asset.locations[0].lon
                            );
                          }}
                          className="marker-btn"
                        >
                          View Timeline
                        </button>
                      </TableCell>
                    </TableRow>
                  </>
                );
              })}
            </TableBody>
          </Table>
        </Paper>
      </Grid>
    </>
  );
}
