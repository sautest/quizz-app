describe("statistics", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200/sign-in");
    cy.get("#username").type("user1");
    cy.get("#password").type("123");
    cy.get("#action-btn").click();
    cy.wait(100);
  });

  it("user should be able to switch between several stat charts", () => {
    cy.visit("http://localhost:4200/statistics/quiz/685");
    cy.wait(500);
    cy.get("#pie-chart").should("exist");
    cy.get("#pie-grid-chart").should("not.exist");
    cy.get("#pie-bar-horizontal-chart").should("not.exist");
    cy.get("#pie-bar-vertical-chart").should("not.exist");

    cy.get(".fa-circle-half-stroke").click();

    cy.get("#pie-chart").should("not.exist");
    cy.get("#pie-grid-chart").should("exist");
    cy.get("#pie-bar-horizontal-chart").should("not.exist");
    cy.get("#pie-bar-vertical-chart").should("not.exist");

    cy.get(".fa-chart-bar").click();

    cy.get("#pie-chart").should("not.exist");
    cy.get("#pie-grid-chart").should("not.exist");
    cy.get("#pie-bar-horizontal-chart").should("exist");
    cy.get("#pie-bar-vertical-chart").should("not.exist");

    cy.get(".fa-chart-column").click();

    cy.get("#pie-chart").should("not.exist");
    cy.get("#pie-grid-chart").should("not.exist");
    cy.get("#pie-bar-horizontal-chart").should("not.exist");
    cy.get("#pie-bar-vertical-chart").should("exist");
  });

  it("user should be able to toggle stat table", () => {
    cy.visit("http://localhost:4200/statistics/quiz/685");
    cy.wait(1000);
    cy.get(".stat-table").should("not.exist");

    cy.get(".fa-table").click();
    cy.get(".stat-table").should("exist");
  });

  it("user should be able to toggle between charts responses leaderboard", () => {
    cy.visit("http://localhost:4200/statistics/quiz/685");
    cy.wait(1000);
    cy.get(".fa-file-lines").click();
    cy.get(".responses").should("be.visible");
    cy.get(".leaderboard-table").should("not.be.visible");

    cy.get(".fa-medal").click();
    cy.get(".responses").should("not.be.visible");
    cy.get(".leaderboard-table").should("be.visible");
  });
});
