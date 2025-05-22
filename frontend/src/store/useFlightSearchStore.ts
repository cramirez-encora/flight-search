import { create } from 'zustand';

export type AirportOption = {
    name: string;
    iataCode: string;
    city: string;
    country: string;
};

type FlightSearchState = {
    origin: AirportOption | null;
    destination: AirportOption | null;
    departureDate: string;
    returnDate: string;
    currencyCode: 'USD' | 'MXN' | 'EUR';
    adults: number;
    nonStop: boolean;
    setField: <K extends keyof FlightSearchState>(key: K, value: FlightSearchState[K]) => void;
};

export const useFlightSearchStore = create<FlightSearchState>((set) => ({
    origin: null,
    destination: null,
    departureDate: '',
    returnDate: '',
    currencyCode: 'USD',
    adults: 1,
    nonStop: false,
    setField: (key, value) => set({ [key]: value }),
}));
