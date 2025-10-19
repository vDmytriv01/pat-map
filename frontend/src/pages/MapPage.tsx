import { useEffect, useMemo, useState } from 'react';
import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import toast from 'react-hot-toast';
import { fetchPlaces } from '../api/places';
import { Place } from '../types';
import { PageContainer } from '../components/PageContainer';

const markerIcon = new L.Icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

const defaultCenter: [number, number] = [51.505, -0.09];

export const MapPage: React.FC = () => {
  const [places, setPlaces] = useState<Place[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadPlaces = async () => {
      setIsLoading(true);
      try {
        const data = await fetchPlaces();
        setPlaces(data);
      } catch (error) {
        toast.error('Failed to load places.');
        console.error(error);
      } finally {
        setIsLoading(false);
      }
    };

    loadPlaces();
  }, []);

  const mapCenter = useMemo(() => {
    if (places.length === 0) {
      return defaultCenter;
    }
    const [first] = places;
    return [first.latitude, first.longitude] as [number, number];
  }, [places]);

  return (
    <PageContainer title="Explore the map">
      {isLoading ? (
        <p className="text-sm text-slate-600">Loading map...</p>
      ) : (
        <div className="h-[600px] overflow-hidden rounded-lg shadow">
          <MapContainer center={mapCenter} zoom={6} scrollWheelZoom className="h-full w-full">
            <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {places.map((place) => (
              <Marker key={place.id} position={[place.latitude, place.longitude]} icon={markerIcon}>
                <Popup>
                  <div className="space-y-1">
                    <h3 className="text-base font-semibold text-slate-800">{place.name}</h3>
                    <p className="text-sm text-slate-600">{place.description ?? 'No description provided.'}</p>
                    <p className="text-xs text-slate-500">
                      {place.latitude.toFixed(4)}, {place.longitude.toFixed(4)}
                    </p>
                  </div>
                </Popup>
              </Marker>
            ))}
          </MapContainer>
        </div>
      )}
    </PageContainer>
  );
};
