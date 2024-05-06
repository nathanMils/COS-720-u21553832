// cypress/integration/e2e/login.e2e.spec.ts

describe('Login View', () => {
  beforeEach(() => {
    // Visit the login page before each test
    cy.visit('/login')
  })

  it('Does not log in with invalid credentials', () => {
    // Type into the username and password fields
    cy.get('#username').type('wronguser')
    cy.get('#password').type('wrongpassword')

    // Click on the login button
    cy.get('button[type="submit"]').click()

    // Check if an error message is displayed
    cy.url().should('include', '/login')
  })

  it('Logs in with valid credentials and accesses protected page and then logs out', () => {
    // Type into the username and password fields
    cy.get('#username').type('NathanOpp')
    cy.get('#password').type('Stupidcat12!')

    // Click on the login button
    cy.get('button[type="submit"]').click()

    // Check if the URL is now the dashboard page
    cy.url().should('include', '/dashboard')

    // Visit a protected page
    cy.visit('/dashboard')

    // Check if the user can access the protected page
    cy.url().should('include', '/dashboard')


    // Visit a protected page
    cy.visit('/profile')

    // Check if the user can access the protected page
    cy.url().should('include', '/profile')

    // Click on the logout button
    cy.get('button').contains('Logout').click()

    cy.get('button').contains('Confirm').click()

    // Check if the URL is now the login page
    cy.url().should('include', '/login')

  })
})