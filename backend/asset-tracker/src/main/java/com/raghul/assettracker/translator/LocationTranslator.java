package com.raghul.assettracker.translator;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import com.mapbox.turf.TurfJoins;
import com.raghul.assettracker.dto.AssetDTO;
import com.raghul.assettracker.dto.AssetGeofenceDTO;
import com.raghul.assettracker.dto.AssetRouteDTO;
import com.raghul.assettracker.dto.CoordinateDTO;
import com.raghul.assettracker.dto.GeofenceDTO;
import com.raghul.assettracker.dto.LocationAssetDTO;
import com.raghul.assettracker.dto.LocationDTO;
import com.raghul.assettracker.dto.OrderedCoordinateDTO;
import com.raghul.assettracker.manager.LocationManager;
import com.raghul.assettracker.manager.ReferenceManager;
import com.raghul.assettracker.model.Asset;
import com.raghul.assettracker.model.AssetGeofenceModel;
import com.raghul.assettracker.model.AssetLocation;
import com.raghul.assettracker.model.AssetRouteModel;
import com.raghul.assettracker.model.AssetRoutePathModel;
import com.raghul.assettracker.model.RefTerm;
import com.raghul.assettracker.repository.AssetRepository;
import com.raghul.assettracker.util.CommonConstants;

@Component
public class LocationTranslator {
	
	@Resource
	ReferenceManager referenceManager;
	
	@Resource
	AssetRepository assetRepository;
	
	@Resource
	LocationManager locationManager;
	
	@Resource 
	AssetTranslator assetTranslator;
	
	public AssetDTO translateToAssetDTO(AssetLocation assetLocation,Map<UUID,RefTerm> values) {
		AssetDTO assetDTO = new AssetDTO();
		assetDTO.setAssetName(assetLocation.getAsset().getAssetName());
		assetDTO.setAssetType(values.get(assetLocation.getAsset().getAssetType()).getRefTermValue());
		assetDTO.setId(assetLocation.getAsset().getAssetId().toString());
	    LocationDTO locationDTO = new LocationDTO();
	    locationDTO.setLat(assetLocation.getLocation().getY());
	    locationDTO.setLon(assetLocation.getLocation().getX());
	    locationDTO.setTime(assetLocation.getCreatedOn().toString());
	    assetDTO.setLocations(Collections.singletonList(locationDTO));
		return assetDTO;
	}
	
	public List<AssetDTO> translateToAssetDTOs(List<AssetLocation> locations){
		Map<UUID,RefTerm> values =referenceManager. getRefTermValue(CommonConstants.ASSET_TYPE);
		List<AssetDTO> assets = new ArrayList<>();
		locations.forEach(location ->{
			assets.add(translateToAssetDTO(location,values));
		});
		return assets;
	}
	
	public AssetDTO translateToAssetDTO(List<AssetLocation> locations,Asset asset) {

		Map<UUID,RefTerm> values = referenceManager.getRefTermValue(CommonConstants.ASSET_TYPE);
		AssetDTO assetDTO = new AssetDTO();
		assetDTO.setAssetName(asset.getAssetName());
		assetDTO.setId(asset.getAssetId().toString());
		assetDTO.setAssetType(values.get(asset.getAssetType()).getRefTermValue());
		List<LocationDTO> locationDTOs = new ArrayList<>();
		locations.forEach(
				item -> {
					locationDTOs.add(translateToLocationDTO(item));
				}
				);
		assetDTO.setLocations(locationDTOs);
		return assetDTO;
	}
	
	public LocationDTO translateToLocationDTO(AssetLocation assetLocation) {
		LocationDTO locationDTO = new LocationDTO();
		locationDTO.setLon(assetLocation.getLocation().getX());
		locationDTO.setLat(assetLocation.getLocation().getY());
		locationDTO.setTime(assetLocation.getCreatedOn().toString());
		return locationDTO;
		
		
	}
	
	
	
	public AssetLocation translateToAssetLocation(LocationAssetDTO locationAssetDTO) {
		AssetLocation assetLocation  = new AssetLocation();
		RefTerm systemUser = referenceManager.findRefTermByValue(CommonConstants.SYSTEM_USER);
		assetLocation.setAssetLocationId(UUID.randomUUID());
		assetLocation.setCreatedOn(new Timestamp(Calendar.getInstance().getTime().getTime()));
		Point point = new GeometryFactory().createPoint(new Coordinate(locationAssetDTO.getLon(),locationAssetDTO.getLat()));
		assetLocation.setLocation(point);
		assetLocation.setAsset(assetRepository.findByAssetId(UUID.fromString(locationAssetDTO.getAssetId())));
		assetLocation.setCreatedBy(systemUser.getRefTermId().toString());
		return assetLocation;
	}
	
	public List<GeofenceDTO> translateToAssetGeofenceDTOs(List<AssetGeofenceModel> assetsGeofence){
		if(assetsGeofence!=null && !assetsGeofence.isEmpty()) {
			List<GeofenceDTO> assetGeofencesDTOs = new ArrayList<>();
			assetsGeofence.forEach(
					assetGeofence->{
						assetGeofencesDTOs.add(translateToAssetGeofenceDTO(assetGeofence));
					}
		
					);
			return assetGeofencesDTOs;
		}
		return Collections.emptyList();
	}
	
	public GeofenceDTO translateToAssetGeofenceDTO(AssetGeofenceModel assetGeofenceModel) {
		 List<CoordinateDTO> coordinates = new ArrayList<>();
		    for(Coordinate cor:assetGeofenceModel.getGeoFence().getCoordinates()) {
		    	CoordinateDTO coordinate = new CoordinateDTO();
		    	coordinate.setLat(cor.getY());
		    	coordinate.setLon(cor.getX());
		    	coordinates.add(coordinate);
		    }
		    
		    GeofenceDTO assetGeofenceDTO = new GeofenceDTO();
		    assetGeofenceDTO.setCoordinates(coordinates);
		    assetGeofenceDTO.setAssetId(assetGeofenceModel.getAssetId().toString());
		    return assetGeofenceDTO;   
	}
	
	public AssetGeofenceModel translateToAssetGeofenceModel(GeofenceDTO assetGeofenceDTO) {
		GeometryFactory geometryFactory = new GeometryFactory();
		Coordinate coordinates[] = new Coordinate[assetGeofenceDTO.getCoordinates().size()];
		for(int i= 0;i<assetGeofenceDTO.getCoordinates().size();i++) {
			CoordinateDTO coordinate = assetGeofenceDTO.getCoordinates().get(i);
			coordinates[i] = new Coordinate(coordinate.getLon(),coordinate.getLat());
		}
		Polygon polygon = geometryFactory.createPolygon(coordinates);
	   AssetGeofenceModel assetGeofenceModel = locationManager.getAssetGeofenceByAssetId(UUID.fromString(assetGeofenceDTO.getAssetId()));
	   if(assetGeofenceModel == null) {
		   assetGeofenceModel = new AssetGeofenceModel();
		   assetGeofenceModel.setAssetGeofenceId(UUID.randomUUID());
	   }
	   assetGeofenceModel.setGeoFence(polygon);
	   assetGeofenceModel.setCreatedBy(referenceManager.findRefTermByValue(CommonConstants.SYSTEM_USER).getRefTermId().toString());
	   assetGeofenceModel.setAssetId(UUID.fromString(assetGeofenceDTO.getAssetId()));
	   return assetGeofenceModel;
	}
	
	
	public AssetGeofenceDTO translateToAssetGeofenceDTO(AssetLocation assetLocation,Asset asset,AssetGeofenceModel geofence) {
		GeofenceDTO geofenceDTO = translateToAssetGeofenceDTO(geofence);
		AssetDTO assetDTO =  assetTranslator.translateToAssetDTO(asset);
		com.mapbox.geojson.Point point = com.mapbox.geojson.Point.fromLngLat(assetLocation.getLocation().getX(), assetLocation.getLocation().getY());
		AssetGeofenceDTO assetGeofenceDTO = new AssetGeofenceDTO();
		
		List<com.mapbox.geojson.Point> points = new ArrayList<>();
		for(Coordinate cor:geofence.getGeoFence().getCoordinates()) {
			points.add(com.mapbox.geojson.Point.fromLngLat(cor.getX(), cor.getY()));
		}
		List<List<com.mapbox.geojson.Point>> values  = new ArrayList<>();
		values.add(points);
		com.mapbox.geojson.Polygon poly = com.mapbox.geojson.Polygon.fromLngLats(values);
        assetGeofenceDTO.setIsWithinFence(TurfJoins.inside(point, poly));
        assetGeofenceDTO.setGeofenceDTO(geofenceDTO);
        assetGeofenceDTO.setAsset(assetDTO);
       return assetGeofenceDTO;
        
	}
	
	public AssetRouteModel translateToAssetRouteModel(AssetRouteDTO assetRoute) {
		RefTerm refVal = referenceManager.findRefTermByValue(CommonConstants.SYSTEM_USER);
		AssetRouteModel routeModel = new AssetRouteModel();
		routeModel.setAssetId(UUID.fromString(assetRoute.getAssetId()));
		routeModel.setAssetRouteId(UUID.randomUUID());
		routeModel.setIsDestinationReached(false);
		routeModel.setCreatedBy(refVal.getRefTermId().toString());
		routeModel.setLocationSrc(new GeometryFactory().createPoint(new Coordinate(assetRoute.getSrc().getLon(),assetRoute.getSrc().getLat())));
		routeModel.setLocationDest(new GeometryFactory().createPoint(new Coordinate(assetRoute.getDest().getLon(),assetRoute.getDest().getLat())));
		return routeModel;
	}
	
	public AssetRoutePathModel translateToAssetRoutePathModel(UUID assetRouteId,Coordinate coordinate,Integer order) {
		RefTerm refVal = referenceManager.findRefTermByValue(CommonConstants.SYSTEM_USER);
		AssetRoutePathModel pathModel = new AssetRoutePathModel();
	  pathModel.setAssetRouteId(assetRouteId);
	  pathModel.setAssetRoutePathId(UUID.randomUUID());
	  pathModel.setCreatedBy(refVal.getRefTermId().toString());
	  pathModel.setSortOrder(order);
	  pathModel.setLocation(new GeometryFactory().createPoint(coordinate));
	  return pathModel;
	}
	
	public List<AssetRoutePathModel> translateToAssetRoutePath(AssetRouteModel routeModel,AssetRouteDTO route){
		
	  if(route != null && route.getRoutes() != null && !route.getRoutes().isEmpty()) {
		  List<AssetRoutePathModel> routeValues = new ArrayList<>();
		  route.getRoutes().forEach(
				  location->{
					 routeValues.add(translateToAssetRoutePathModel(routeModel.getAssetRouteId(),new Coordinate(location.getCoordinate().getLon(),location.getCoordinate().getLat()),location.getOrder()));
				  }
				  );
		  System.out.println(routeValues.size());
		  return routeValues;
	  }
	  
	  return Collections.emptyList();
	}
	
	
	public AssetRouteDTO translateToRouteDTO(AssetRouteModel routeModel,List<AssetRoutePathModel> pathModel) {
		AssetRouteDTO routeDTO = new AssetRouteDTO();
		CoordinateDTO srcCor = new CoordinateDTO();
		srcCor.setLat(routeModel.getLocationSrc().getY());
		srcCor.setLon(routeModel.getLocationSrc().getX());
		routeDTO.setSrc(srcCor);
		CoordinateDTO destCor = new CoordinateDTO();
		destCor.setLat(routeModel.getLocationDest().getY());
		destCor.setLon(routeModel.getLocationDest().getX());
		routeDTO.setDest(destCor);
		routeDTO.setAssetId(routeModel.getAssetId().toString());
		List<OrderedCoordinateDTO> routes = new ArrayList<>();
		if(pathModel != null && !pathModel.isEmpty()) {
			for(AssetRoutePathModel path: pathModel) {
				CoordinateDTO cord = new CoordinateDTO();
				System.out.println("start");
				System.out.println(path.getLocation().getX());
				System.out.println(path.getLocation().getY());
				System.out.println("end");
				cord.setLat(path.getLocation().getY());
				cord.setLon(path.getLocation().getX());
				OrderedCoordinateDTO ordered = new OrderedCoordinateDTO();
				ordered.setCoordinate(cord);
				routes.add(ordered);
				

			}
		}
		
		routeDTO.setRoutes(routes);
		routeDTO.setIsDestinationReached(routeModel.getIsDestinationReached());
		return routeDTO;
	}

	
	public AssetRouteDTO translateToAssetRouteDTO(List<AssetRoutePathModel> paths,CoordinateDTO coordinate) {
		List<com.mapbox.geojson.Point> points = new ArrayList<>();
		paths.forEach( 
				path->{
					points.add(com.mapbox.geojson.Point.fromLngLat(path.getLocation().getX(), path.getLocation().getY()));
				}
				);
		com.mapbox.geojson.Point assetPoint = com.mapbox.geojson.Point.fromLngLat(coordinate.getLon(), coordinate.getLat());
		AssetRouteDTO routeDTO = new AssetRouteDTO();
		routeDTO.setIsWithinRoute(pointInsideLine(assetPoint, points));
	    return routeDTO;
	}
	
	
private boolean pointInsideLine(com.mapbox.geojson.Point assetPoint,List<com.mapbox.geojson.Point> points) {
		
	DecimalFormat df = new DecimalFormat("#.#####");
		for(com.mapbox.geojson.Point point:points) {
			if(df.format(point.latitude()).equals( df.format(assetPoint.latitude())) && df.format(point.longitude()) .equals( df.format(assetPoint.longitude()))) {
				return true;
			}
		}
		
		return false;
	}
	
	

}
