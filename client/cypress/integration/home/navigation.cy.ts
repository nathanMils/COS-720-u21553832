describe('Routing', () => {
  it('Visits the home page', () => {
    cy.visit('/')
    cy.url().should('eq', 'http://localhost:3000/')
  })

  it('Navigates to the login page', () => {
    cy.visit('/login')
    cy.url().should('eq', 'http://localhost:3000/login')
  })

  it('Navigates to the apply page', () => {
    cy.visit('/apply')
    cy.url().should('eq', 'http://localhost:3000/apply')
  })

  // Add more tests for other routes...
})