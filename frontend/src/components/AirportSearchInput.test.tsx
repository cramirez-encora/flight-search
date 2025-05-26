import { render, screen, fireEvent, waitFor } from "@testing-library/react"
import { vi } from "vitest"
import { AirportSearchInput } from "./AirportSearchInput"
import { useFlightSearchStore } from "@/store/useFlightSearchStore"

vi.mock("@/store/useFlightSearchStore", () => {
    return {
        useFlightSearchStore: vi.fn(() => ({
            origin: null,
            destination: null,
            setField: vi.fn()
        }))
    }
})

// Mock fetch
const mockAirports = [
    { name: "LOS ANGELES INTERNATIONAL", iataCode: "LAX", city: "LOS ANGELES", country: "USA" },
    { name: "LONDON HEATHROW", iataCode: "LHR", city: "LONDON", country: "UK" }
]

beforeEach(() => {
    global.fetch = vi.fn(() =>
        Promise.resolve({
            json: () => Promise.resolve(mockAirports)
        })
    ) as unknown as typeof fetch
})

describe("AirportSearchInput", () => {
    it("renders with label", () => {
        render(<AirportSearchInput label="Departure Airport" type="origin" />)
        expect(screen.getByText("Departure Airport")).toBeInTheDocument()
        expect(screen.getByRole("combobox")).toBeInTheDocument()
    })

    it("opens popover and displays search results", async () => {
        render(<AirportSearchInput label="Departure Airport" type="origin" />)

        fireEvent.click(screen.getByRole("combobox"))
        fireEvent.change(screen.getByPlaceholderText("Search airport..."), {
            target: { value: "LAX" },
        })

        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledWith(
                "/api/airports/search?keyword=LAX",
                expect.any(Object)
            )
        })

        await waitFor(() => {
            const items = screen.getAllByRole("option") // CommandItem has role="option"
            const losAngelesItem = items.find((item) =>
                item.textContent?.includes("LOS ANGELES INTERNATIONAL") &&
                item.textContent?.includes("(LAX)") &&
                item.textContent?.includes("LOS ANGELES") &&
                item.textContent?.includes("USA")
            )
            expect(losAngelesItem).toBeDefined()
        })
    })

    it("selects an airport and calls setField", async () => {
        const setFieldMock = vi.fn()
        ;(useFlightSearchStore as any).mockReturnValue({
            origin: null,
            destination: null,
            setField: setFieldMock
        })

        render(<AirportSearchInput label="Departure Airport" type="origin" />)

        fireEvent.click(screen.getByRole("combobox"))
        fireEvent.change(screen.getByPlaceholderText("Search airport..."), {
            target: { value: "LAX" }
        })

        await waitFor(() => {
            expect(screen.getByText(/LOS ANGELES INTERNATIONAL/)).toBeInTheDocument()
        })

        fireEvent.click(screen.getByText(/LOS ANGELES INTERNATIONAL/))

        expect(setFieldMock).toHaveBeenCalledWith("origin", mockAirports[0])
    })
})
