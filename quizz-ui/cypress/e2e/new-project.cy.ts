describe("New Project", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200/sign-in");
    cy.get("#username").type("user1");
    cy.get("#password").type("123");
    cy.get("#action-btn").click();
    cy.wait(100);
  });

  it("user should be able create a new Quiz", () => {
    cy.visit("http://localhost:4200/create");

    cy.get("#project-type").click();
    cy.contains("Quiz").click();
    cy.get("#project-title").type("cypress-test");
    cy.get("#create-btn").click();

    cy.location().should(location => {
      expect(location.href).to.contain("http://localhost:4200/create/quiz/");
    });
    cy.get("#toast-container").should("contain", "Quiz Created!");
    cy.get(".edit-tabview").should("exist");
  });

  it("user should be able create a new question", () => {
    cy.visit("http://localhost:4200/create/quiz/685");

    cy.get("#add-question-btn").click();

    cy.get("#question-type-dropdown").click();
    cy.contains("Single Choice").click();
    cy.get("#score").type("5");
    cy.get("#question-name").type("10");
    cy.get("#add-option-btn").click();
    cy.get("#add-option-btn").click();

    cy.get(".option-input").eq(0).type("1 option");
    cy.get(".option-checkbox").eq(0).click();
    cy.get(".option-input").eq(1).type("2 option");
    cy.get(".option-input").eq(2).type("3 option");
    cy.get("#save-btn").click();

    cy.get("#toast-container").should("contain", "Question Created!");
  });

  it("user should be able edit a  question", () => {
    cy.visit("http://localhost:4200/create/quiz/685");

    cy.get(".pi-file-edit").eq(0).click();
    cy.get("#question-type-dropdown").click();
    cy.contains("Multiple Choice").click();
    cy.get("#question-name").type("question?");
    cy.get(".option-input").eq(0).type("edited option");
    cy.get("#save-btn").click();

    cy.get("#toast-container").should("contain", "Question Updated!");
  });

  it("user should be able to add question logic", () => {
    cy.visit("http://localhost:4200/create/quiz/685");

    cy.get(".pi-sitemap").eq(0).click();
    cy.get(".add-btn").click();
    cy.get(".condition").eq(0).click();
    cy.contains("Always").click();
    cy.get(".action").eq(0).click();
    cy.contains("End questioning").click();
    cy.get("#save-btn").click();

    cy.get("#toast-container").should("contain", "Logic Updated!");
  });

  it("user should be able to delete question", () => {
    cy.visit("http://localhost:4200/create/quiz/685");

    cy.get(".pi-times").eq(0).click();
    cy.get(".p-button-danger").click();

    cy.get("#toast-container").should("contain", "Question Deleted!");
  });

  it("user should be able to edit quiz settings", () => {
    cy.visit("http://localhost:4200/create/quiz/685");

    cy.get(".pi-cog").click();
    cy.get(".project-name").clear().type("edited project");
    cy.get(".show-answers-switch").click();
    cy.get(".basic-info-switch").click();
    cy.get(".save-btn").click();

    cy.get("#toast-container").should("contain", "Settings Updated!");
    cy.get(".project-name").should("have.value", "edited project");
  });

  it("user should be able to edit user profile", () => {
    cy.visit("http://localhost:4200/profile/1");

    cy.get("#email").clear().type("user1234@gmail.com");
    cy.get(".save-btn").click();

    cy.get("#toast-container").should("contain", "Profile updated!");
    cy.get("#email").should("have.value", "user1234@gmail.com");
  });
});
