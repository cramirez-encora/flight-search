import { render, screen, fireEvent } from '@testing-library/react';
import { FlightCard } from './FlightCard';
import type { FlightSearchResponse } from '@/store/useFlightStore';
import { vi } from 'vitest';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const mockDeparture: FlightSearchResponse = {
    uuid: 'abc123',
    departureTime: '08:00',
    arrivalTime: '10:00',
    departureAirportName: 'Los Angeles Intl',
    departureAirportCode: 'LAX',
    arrivalAirportName: 'JFK Airport',
    arrivalAirportCode: 'JFK',
    airlineName: 'Delta',
    airlineCode: 'DL',
    operatingAirlineName: 'SkyTeam',
    operatingAirlineCode: 'ST',
    duration: '2h',
    stops: 0,
    totalPrice: '500',
    pricePerTraveler: '250',
};

const mockReturn: FlightSearchResponse = {
    ...mockDeparture,
    uuid: 'def456',
    departureTime: '16:00',
    arrivalTime: '18:00',
    departureAirportName: 'JFK Airport',
    departureAirportCode: 'JFK',
    arrivalAirportName: 'Los Angeles Intl',
    arrivalAirportCode: 'LAX',
    duration: '2h',
    stops: 1,
};

describe('<FlightCard />', () => {
    it('renders departure and return segments correctly', () => {
        render(<FlightCard flights={[mockDeparture, mockReturn]} />);

        expect(screen.getByText(/departure/i)).toBeInTheDocument();
        expect(screen.getByText(/return/i)).toBeInTheDocument();

        expect(screen.getByText(/08:00 - 10:00/)).toBeInTheDocument(); // departure times
        expect(screen.getByText(/16:00 - 18:00/)).toBeInTheDocument(); // return times

        expect(screen.getByText(/Los Angeles Intl \(LAX\) - JFK Airport \(JFK\)/)).toBeInTheDocument();
        expect(screen.getByText(/JFK Airport \(JFK\) - Los Angeles Intl \(LAX\)/)).toBeInTheDocument();
        expect(screen.getByText((text) => text.includes('Nonstop'))).toBeInTheDocument();
        expect(screen.getByText((text) => text.includes('1 stop'))).toBeInTheDocument();
    });

    it('navigates to details page on button click', () => {
        render(<FlightCard flights={[mockDeparture, mockReturn]} />);
        const button = screen.getByRole('button', { name: /view details/i });
        fireEvent.click(button);
        expect(mockNavigate).toHaveBeenCalledWith('/details/abc123');
    });

    it('renders only departure if returnFlight is missing', () => {
        render(<FlightCard flights={[mockDeparture]} />);
        expect(screen.getByText(/departure/i)).toBeInTheDocument();
        expect(screen.queryByText(/return/i)).not.toBeInTheDocument();
    });
});

