import { useFlightStore } from '@/store/useFlightStore';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { FlightCard } from '@/components/FlightCard';

export const ResultsPage = () => {
    const results = useFlightStore((state) => state.results);
    const navigate = useNavigate();

    if (results.length === 0) {
        return (
            <div className="text-center mt-10">
                <p>No flight results available.</p>
                <Button onClick={() => navigate('/')}>Back to Search</Button>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto py-8 space-y-4">
            <h2 className="text-2xl font-bold mb-6">Flight Results</h2>

            {results.map((flight) => (
                <FlightCard key={flight.uuid} flight={flight} />
            ))}
        </div>
    );
};

export default ResultsPage;
