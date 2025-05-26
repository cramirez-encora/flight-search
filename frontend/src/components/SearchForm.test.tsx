import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent} from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { SearchForm } from './SearchForm';


const mockSetField = vi.fn();
const mockSetResults = vi.fn();
const mockNavigate = vi.fn();

vi.mock('@/store/useFlightSearchStore', () => ({
    useFlightSearchStore: () => ({
        origin: { iataCode: 'JFK' },
        destination: { iataCode: 'LAX' },
        departureDate: '2025-06-01',
        currencyCode: 'USD',
        adults: 2,
        nonStop: false,
        setField: mockSetField,
    }),
}));

vi.mock('@/store/useFlightStore', () => ({
    useFlightStore: () => ({
        setResults: mockSetResults,
    }),
}));

vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

beforeEach(() => {
    vi.clearAllMocks();
});

describe('SearchForm', () => {
    it('renders all form fields', () => {
        render(<SearchForm />, { wrapper: BrowserRouter });

        expect(screen.getByText(/departure airport/i)).toBeInTheDocument();
        expect(screen.getByText(/arrival airport/i)).toBeInTheDocument();
        expect(screen.getByText(/departure date/i)).toBeInTheDocument();
        expect(screen.getByText(/return date/i)).toBeInTheDocument();
        expect(screen.getByText(/adults/i)).toBeInTheDocument();
        expect(screen.getByText(/currency/i)).toBeInTheDocument();
        expect(screen.getByText(/non-stop flights only/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /search flights/i })).toBeInTheDocument();
    });

    it('toggles non-stop checkbox', () => {
        render(<SearchForm />, { wrapper: BrowserRouter });

        const checkbox = screen.getByRole('checkbox', { name: /non-stop/i });
        fireEvent.click(checkbox);

        expect(mockSetField).toHaveBeenCalledWith('nonStop', true);
    });

    it('shows currency options when clicking the currency select', async () => {
        render(<SearchForm />, { wrapper: BrowserRouter });

        const trigger = screen.getByTestId('currency-select-trigger');
        fireEvent.click(trigger);

        expect(await screen.findByTestId('currency-option-mxn')).toBeInTheDocument();
        expect(await screen.findByTestId('currency-option-usd')).toBeInTheDocument();
        expect(await screen.findByTestId('currency-option-eur')).toBeInTheDocument();
    });

});
