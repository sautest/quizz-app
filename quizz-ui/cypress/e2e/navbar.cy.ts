describe("Navbar", () => {
  it("hamburger btn should appear on smaller screens", () => {
    cy.visit("http://localhost:4200/sign-in");

    cy.viewport(550, 750);

    cy.get(".p-menubar-button").should("exist");
  });
});
