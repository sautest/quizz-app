describe("statistics", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200/quiz/9e2af351-3100-49ee-844c-27da7775ab05");
  });

  it("user should be able to enter user info and start solving quiz", () => {
    cy.get("#user-name").type("user000");
    cy.get("#user-age").type("33");
    cy.get(".start-btn").click();
    cy.get(".question-form").should("exist");
  });

  it("user should be able to solve the quiz", () => {
    cy.get("#user-name").type("user000");
    cy.get("#user-age").type("33");
    cy.get(".start-btn").click();

    cy.get(".radio-btn").eq(0).click();
    cy.get(".next-btn").click();
    cy.get(".radio-btn").eq(0).click();
    cy.get(".next-btn").click();
    cy.get(".radio-btn").eq(0).click();
    cy.get(".next-btn").click();

    cy.get(".results-message").should("exist");
  });
});
