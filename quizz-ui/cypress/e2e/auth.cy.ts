describe("Auth", () => {
  it("user should be able to successfully Register", () => {
    cy.visit("http://localhost:4200/sign-up");

    cy.get("#email").type("user101@gmail.com");
    cy.get("#username").type("user101");
    cy.get("#password").type("123");
    cy.get("#action-btn").click();

    cy.location().should(location => {
      expect(location.href).to.contain("http://localhost:4200/sign-in");
    });

    cy.get("#toast-container").should("contain", "Account Created!");
  });

  it("user should be able to successfully login", () => {
    cy.visit("http://localhost:4200/sign-in");

    cy.get("#username").type("user1");
    cy.get("#password").type("123");
    cy.get("#action-btn").click();

    cy.location().should(location => {
      expect(location.href).to.contain("http://localhost:4200/dashboard/");
    });
    cy.get(".dashboard").should("exist");
    cy.get("#toast-container").should("contain", "Logged In!");
  });

  it("user should be able to successfully log out", () => {
    cy.visit("http://localhost:4200/sign-in");

    cy.get("#username").type("user1");
    cy.get("#password").type("123");
    cy.get("#action-btn").click();

    cy.contains("Log out").click();

    cy.location().should(location => {
      expect(location.href).to.contain("http://localhost:4200/home");
    });
    cy.get(".dashboard").should("not.exist");
  });
});
