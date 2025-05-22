import { create } from 'zustand';

export interface FlightSearchResponse {
    departureTime: string;
    arrivalTime: string;
    departureAirportName: string;
    departureAirportCode: string;
    arrivalAirportName: string;
    arrivalAirportCode: string;
    airlineName: string;
    airlineCode: string;
    operatingAirlineName?: string;
    operatingAirlineCode?: string;
    duration: string;
    stops: number;
    totalPrice: string;
    pricePerTraveler: string;
    uuid: string;
}

interface FlightStore {
    results: FlightSearchResponse[];
    setResults: (results: FlightSearchResponse[]) => void;
}

export const useFlightStore = create<FlightStore>((set) => ({
    results: [],
    setResults: (results) => set({ results }),
}));
