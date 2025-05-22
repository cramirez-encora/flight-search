import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SearchPage from './pages/SearchPage';
import ResultsPage from './pages/ResultsPage';
import FlightDetailsPage from "@/pages/FlightDetailsPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<SearchPage />} />
                <Route path="/results" element={<ResultsPage />} />
                <Route path="/details/:uuid" element={<FlightDetailsPage />} />
            </Routes>
        </Router>
    );
}

export default App;
