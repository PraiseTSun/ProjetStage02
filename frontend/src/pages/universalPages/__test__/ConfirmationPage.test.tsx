import ConfirmationPage from "../ConfirmationPage";
import {act, render, screen} from "@testing-library/react";
import React from "react";
import {BrowserRouter} from "react-router-dom";

describe("ConfirmationPage test", () => {
    beforeEach(async () => {
        global.fetch = jest.fn(() => {
                return Promise.resolve({
                    ok: true,
                });
            }
        ) as jest.Mock;

        window.alert = jest.fn(() => null) as jest.Mock;

        await act(async () => {
            render(<ConfirmationPage/>, {wrapper: BrowserRouter})
        });
    });

    it("ConfirmationPageLayoutTest", async () => {
        expect(await screen.findByText(/Succès! Vous pouvez fermez cette page/i)).toBeInTheDocument()
    })
});
